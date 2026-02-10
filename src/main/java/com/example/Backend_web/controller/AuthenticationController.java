package com.example.Backend_web.controller;

import com.example.Backend_web.dto.request.ApiResponse;
import com.example.Backend_web.dto.request.AuthenticationRequest;
import com.example.Backend_web.dto.request.IntrospectRequest;
import com.example.Backend_web.dto.response.AuthenticationResponse;
import com.example.Backend_web.dto.response.IntrospectResponse;
import com.example.Backend_web.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    //end point đăng nhập
//    @PostMapping("/token")
//    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
//        var result = authenticationService.authenticate(request);
//        return ApiResponse.<AuthenticationResponse>builder()
//                .result(result)
//                .build();
//    }

    //End point đăng nhập tài khoản
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authResponse = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authResponse)
                .build();
    }

    // end point xác thực token để đăng nhập
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    //EndPoint Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        // Không có token → coi như logout thành công
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok("Logout success");
        }

        String token = authHeader.substring(7);
        authenticationService.logout(token);

        return ResponseEntity.ok("Logout success");
    }

    //endpoint logout
//    @PostMapping("/logout")
//    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
//            throws ParseException, JOSEException {
//        authenticationService.logout(request);
//        return ApiResponse.<Void>builder()
//                .build();
//    }

    //Endpoint logout
}
