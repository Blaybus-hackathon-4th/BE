package com.example.blaybus4th.global.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String token = getAccessTokenFromCookie(request);

        // 토큰이 없거나 화이트리스트 대상이면 다음 필터로 진행
        if(token == null){
            filterChain.doFilter(request,response);
        }

        try{

            String decryptedToken = AesUtil.decrypt(token);
            if(jwtTokenProvider.validateToken(decryptedToken)){
                String memberId = jwtTokenProvider.getMemberId(decryptedToken);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        memberId,
                        null,
                        null
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }catch (Exception e){
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request,response);

    }

    private String getAccessTokenFromCookie(HttpServletRequest request){
        if(request.getCookies() == null) return null;
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals("accessToken")){
                return cookie.getValue();
            }
        }
        return null;
    }


}
