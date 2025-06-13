package com.oopsw.clario.config.jwt;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 클라이언트에서 쿠키 전송 허용
        config.addAllowedOrigin("http://localhost:8883"); // 모든 Origin 허용 url로 지정가능
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setExposedHeaders(Arrays.asList("Authorization")); // 클라이언트가 Authorization 헤더 접근 가능
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 위 설정 적용
        return new CorsFilter(source);
    }
}
