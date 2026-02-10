package com.example.Backend_web.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

//User dùng để xem thông tin của chính mình,...
public class SecurityUtil {

//    public static Long getCurrentUserId() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
//            throw new RuntimeException("Unauthenticated");
//        }
//
//        Object idClaim = jwt.getClaims().get("id");
//        if (idClaim == null) {
//            throw new RuntimeException("JWT claims: " + jwt.getClaims());
//        }
//
//        return Long.valueOf(idClaim.toString());
//    }
//
//
//    public static String getCurrentRole() {
//        Authentication auth = SecurityContextHolder
//                .getContext()
//                .getAuthentication();
//
//        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
//            throw new RuntimeException("Unauthenticated");
//        }
//
//        String role = jwt.getClaim("role");
//        if (role == null) {
//            throw new RuntimeException("JWT does not contain role");
//        }
//
//        return role;
//    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new RuntimeException("Unauthenticated");
        }

        return jwt.getSubject(); // sub
    }

}
