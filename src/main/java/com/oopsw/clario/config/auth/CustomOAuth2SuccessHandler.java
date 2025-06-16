package com.oopsw.clario.config.auth;

import com.oopsw.clario.config.jwt.JwtUtil;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();

        // JWT 생성
        String token = jwtUtil.createToken(email);
        log.info("JWT 토큰 생성 완료");

        // 회원 활성화 여부 확인
        Member member = memberRepository.findByEmail(email).orElse(null);
        boolean activated = member != null && Boolean.TRUE.equals(member.getActivation());

        String redirectUrl;
        if (activated) {
            redirectUrl = "http://localhost:8884/index.html?token=" + token;
        } else {
            redirectUrl = "http://localhost:8884/html/account/privacy.html?token=" + token;
        }

        response.sendRedirect(redirectUrl);

        log.info("JWT 토큰: {}", token);
        log.info("활성화 여부: {}", activated);
    }

}
