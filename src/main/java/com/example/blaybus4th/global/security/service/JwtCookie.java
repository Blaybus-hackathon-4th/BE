package com.example.blaybus4th.global.security.service;

import com.example.blaybus4th.global.security.AesUtil;
import com.example.blaybus4th.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtCookie {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${COOKIE_SECURE}")
    private boolean cookieSecure;

    @Value("${JWT_ACCESS_TOKEN_VALIDITY_MS}")
    private long accessTokenValidity;

    @Value("${COOKIE_DOMAIN}")
    private String cookieDomain;

    private String sameSite() {
        return cookieSecure ? "None" : "Lax";
    }


    public void setAccessToken(HttpServletResponse res, String accessToken){
        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from("accessToken", AesUtil.encrypt(accessToken))
                        .httpOnly(true)
                        .secure(cookieSecure)
                        .sameSite(sameSite())
                        .path("/")
                        .maxAge(accessTokenValidity/1000);

        applyDomain(builder);
        res.addHeader("Set-Cookie", builder.build().toString());
    }



    private void applyDomain(ResponseCookie.ResponseCookieBuilder builder) {
        if (cookieDomain != null && !cookieDomain.isBlank()) {
            builder.domain(cookieDomain);
        }
    }


}
