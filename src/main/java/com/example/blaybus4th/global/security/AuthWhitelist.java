package com.example.blaybus4th.global.security;

public class AuthWhitelist {

    private AuthWhitelist() {}

    public static final String[] PATHS = {
            "/",
            "/api/v1/auth/refresh", // accessToken 재발급 허용
            "/api/v1/login",
            "/api/v1/members/login",
            "/api/v1/members/institutions",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/api-docs/swagger-config",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/api/v1/objects/**",
            "/api/v1/quizzes/**"
    };


}
