package com.oopsw.clario.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    // JWT 생성
    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getExpiration());

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    // JWT 검증 후 이메일 추출
    public String getUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null; // 잘못된 토큰
        }
    }

    // 유효한 토큰인지 검사
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
