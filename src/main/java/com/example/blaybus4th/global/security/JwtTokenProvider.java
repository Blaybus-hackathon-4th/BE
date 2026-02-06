package com.example.blaybus4th.global.security;

import com.example.blaybus4th.global.apiPayload.code.GeneralErrorCode;
import com.example.blaybus4th.global.apiPayload.exception.GeneralException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private Key key;
    @Value("${JWT_ACCESS_TOKEN_VALIDITY_MS}")
    private long accessTokenValidity;
    @Value("${JWT_REFRESH_TOKEN_VALIDITY_MS}")
    private long refreshTokenValidity;
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }


    // Access Token 생성
    public String createAccessToken(String memberId, Map<String, Object> extraClaims) {
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidity))
                .signWith(key, SignatureAlgorithm.HS256);

        if (extraClaims != null && !extraClaims.isEmpty()) { // 추가 클레임이 존재할 때만 추가
            jwtBuilder.setClaims(extraClaims);
        }
        return jwtBuilder.compact();
    }

    public String createAccessToken(String memberId) {
        return createAccessToken(memberId, null);
    }

    public String createRefreshToken(String memberId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidity))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(5)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            throw new GeneralException(GeneralErrorCode.ACCESS_TOKEN_EXPIRED);
        }catch (JwtException | IllegalArgumentException e){
            throw new GeneralException(GeneralErrorCode.INVALID_TOKEN_FORMAT);
        } catch (Exception e) {
            throw new GeneralException(GeneralErrorCode.INVALID_AUTHORIZATION_HEADER);
        }
    }

    public String getMemberId(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .setAllowedClockSkewSeconds(5)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch (ExpiredJwtException e){
            throw new GeneralException(GeneralErrorCode.ACCESS_TOKEN_EXPIRED);
        }catch (JwtException | IllegalArgumentException e){
            throw new GeneralException(GeneralErrorCode.INVALID_TOKEN_FORMAT);
        } catch (Exception e) {
            throw new GeneralException(GeneralErrorCode.INVALID_AUTHORIZATION_HEADER);
        }

    }


}
