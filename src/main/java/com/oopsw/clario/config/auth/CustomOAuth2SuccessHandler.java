package com.oopsw.clario.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final HttpSession httpSession;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 세션에서 리다이렉트 URL 꺼내기
        String redirectUrl = (String) httpSession.getAttribute("redirectUrl");
        if (redirectUrl == null) {
            redirectUrl = "/"; // 기본값
        }

        log.info("✅ OAuth 로그인 성공! 리다이렉트 URL: {}", redirectUrl);

        // 세션 정리 (옵션)
        httpSession.removeAttribute("redirectUrl");

        response.sendRedirect(redirectUrl);
    }
}