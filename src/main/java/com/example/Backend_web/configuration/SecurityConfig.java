package com.example.Backend_web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {
            "/users", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/refresh"
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Bật CORS
//                .csrf(AbstractHttpConfigurer::disable) // ✅ Tắt CSRF để API hoạt động đúng
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers(HttpMethod.GET, "/api/products", "/image/**", "/images/**", "/api/images/**").permitAll() // ✅ Cho phép mọi người xem sản phẩm
//                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/categories/add").hasRole("ADMIN") // chỉ admin thêm
//                        .anyRequest()
//                        .authenticated());
//
//        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
//                        .decoder(customJwtDecoder)
//                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
//                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
//        httpSecurity.csrf(AbstractHttpConfigurer::disable);
//
//        return httpSecurity.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self'; " +
                                                "style-src 'self' 'unsafe-inline'; " +
                                                "script-src 'self' 'unsafe-inline';"
                                )
                        )
                )
                .authorizeHttpRequests(request -> request
                        // ✅ Cho phép public các request GET về sản phẩm và ảnh
                        .requestMatchers(HttpMethod.GET,

                                "/api/products/**",
                                "/api/images/**",
                                "/images/**",
                                "/image/**"
                        ).permitAll()

                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        //.anyRequest().authenticated()

                        // ✅ Public POST cho login, introspect, và đăng ký tài khoản
                        .requestMatchers(HttpMethod.POST,
                                "/auth/**",      // cho phép đăng nhập, introspect
                                "/auth/logout",
                                "/users"         // ✅ cho phép đăng ký tài khoản
                        ).permitAll()

                        // ✅ Public POST cho login, register, refresh token
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()

                        // ✅ Admin-only (ví dụ: thêm category)
                        .requestMatchers(HttpMethod.POST, "/api/categories/add").hasRole("ADMIN")

                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        //.requestMatchers("/send-test-email").permitAll() // cho test


                        // ✅ Các request khác yêu cầu phải đăng nhập
                        .anyRequest().authenticated()
                )
                // ✅ Kích hoạt lại OAuth2 Resource Server
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                );


        return httpSecurity.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/products/detail/{slug}").permitAll() // mvcMatcher kiểu path variable
//
//                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
//                        .requestMatchers("/api/images/**", "/images/**", "/image/**").permitAll()
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // preflight
//                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // ✅ OAuth2 Resource Server với JWT
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwtConfigurer -> jwtConfigurer
//                                .decoder(customJwtDecoder)
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                        )
//                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//                );
//
//        return http.build();
//    }


//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//    http
//            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//            .csrf(AbstractHttpConfigurer::disable)
//            .authorizeHttpRequests(auth -> auth
//                    // Public GET endpoints
//                    .requestMatchers("/api/products/**", "/api/images/**").permitAll()
//                    // Public POST endpoints
//                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
//                    // Admin-only POST endpoint
//                    .requestMatchers("/api/categories/add").hasRole("ADMIN")
//                    // Other requests require authentication
//                    .anyRequest().authenticated()
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2
//                    .jwt(jwt -> jwt
//                            .decoder(customJwtDecoder)
//                            .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                    )
//                    .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//            );
//
//    return http.build();
//}




//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
//        corsConfiguration.addAllowedOrigin("http://localhost:3000");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedHeader("*");
//
//        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//
//        return new CorsFilter(urlBasedCorsConfigurationSource);
//    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:5173"); // ✅ Cho phép React truy cập
        corsConfig.addAllowedMethod("*"); // ✅ Cho phép tất cả phương thức HTTP
        corsConfig.addAllowedHeader("*"); // ✅ Cho phép tất cả headers
        corsConfig.setAllowCredentials(true); // ✅ Quan trọng nếu dùng cookie/session

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        // Áp dụng riêng cho `/image/**`
        CorsConfiguration imageCorsConfig = new CorsConfiguration();
        imageCorsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        imageCorsConfig.addAllowedMethod("*");
        imageCorsConfig.addAllowedHeader("*");
//        source.registerCorsConfiguration("/image/**", imageCorsConfig);
        source.registerCorsConfiguration("/images/**", imageCorsConfig);

        return source;
    }


    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }

//    @Bean
//    JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


}
