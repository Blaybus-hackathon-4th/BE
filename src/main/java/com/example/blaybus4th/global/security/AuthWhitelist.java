package com.example.blaybus4th.global.security;

public class AuthWhitelist {

    private AuthWhitelist() {}

    public static final String[] PATHS = {
            "/",
            "/api/v1/auth/refresh", // accessToken 재발급 허용
            "/api/v1/login",
            "/api/v1/members/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
    };


}
