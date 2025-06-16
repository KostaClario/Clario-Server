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

    public String createToken(String email){
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
    }

    public String extractEmail(String token){
        return JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean isValid(String token){
        try{
            JWT.require(Algorithm.HMAC256(jwtProperties.getSecret()))
                    .build()
                    .verify(token);
            return true;
        }catch(JWTVerificationException e){
            return false;
        }
    }
}
