package com.oopsw.clario.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Slf4j
@Component
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {


        log.warn("OAuth2 로그인 실패: {}", exception.getClass().getSimpleName());
        if (exception instanceof SessionAuthenticationException) {
            response.sendRedirect("/loginView?error=session");
        } else {
            response.sendRedirect("/loginView?error=auth");
        }
    }
}
