package com.example.Backend_web.service;

import com.example.Backend_web.dto.request.AuthenticationRequest;
import com.example.Backend_web.dto.request.IntrospectRequest;
import com.example.Backend_web.dto.response.AuthenticationResponse;
import com.example.Backend_web.dto.response.IntrospectResponse;
import com.example.Backend_web.entity.InvalidatedToken;
import com.example.Backend_web.entity.User;
import com.example.Backend_web.exception.AppException;
import com.example.Backend_web.exception.ErrorCode;
import com.example.Backend_web.repository.InvalidatedTokenRepository;
import com.example.Backend_web.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

//    @NonFinal
//    @Value("${jwt.valid-duration}")
//    protected long VALID_DURATION; // giờ, ví dụ 1

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isvalid = true;

        try {
            verifyToken(token);
        } catch(AppException e) {
            isvalid = false;
        }

        return IntrospectResponse.builder()
                .valid(isvalid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        // Lấy role đầu tiên trong Set<String> roles
        //String role = user.getRoles().iterator().next();
        // Lấy role đầu tiên trong Set<Role>
        String role = user.getRoles().iterator().next().name();



        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .id(user.getId())               // Lấy thêm Id Khách hàng
                .username(user.getUsername()) // Lấy thêm Username Khách hàng
                .role(role)
                .build();
    }

//    public void logout(LogoutRequest request) throws ParseException, JOSEException {
//        var signToken = verifyToken(request.getToken());
//        String jit = signToken.getJWTClaimsSet().getJWTID();
//        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
//
//        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
//                .id(jit)
//                .expiryTime(expiryTime)
//                .build();
//
//        invalidatedTokenRepository.save(invalidatedToken);
//    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expityTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("example.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }

    }

//    private String buildScope(User user){
//        StringJoiner stringJoiner = new StringJoiner(" ");
//        if (!CollectionUtils.isEmpty(user.getRoles())) {
//            user.getRoles().forEach(s -> stringJoiner.add(s));
//        }
//        return stringJoiner.toString();
//    }
private String buildScope(User user){
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
        user.getRoles().forEach(r -> stringJoiner.add(r.name())); // đổi s -> r.name()
    }
    return stringJoiner.toString();
}

}
