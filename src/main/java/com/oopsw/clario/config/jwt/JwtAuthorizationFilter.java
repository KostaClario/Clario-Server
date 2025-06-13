package com.oopsw.clario.config.jwt;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromCookie(request);

        if (token != null && jwtUtil.validateToken(token)) {
            String email = jwtUtil.getUsername(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                Member member = memberRepository.findByEmail(email).orElse(null);

                if (member != null && Boolean.TRUE.equals(member.getActivation())) {
                    CustomOAuth2User principal = new CustomOAuth2User(
                            member.getName(),
                            member.getEmail(),
                            null, // attributes 없음
                            Collections.singleton(() -> member.getRoleKey())
                    );

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    log.info("JWT 인증 성공: {}", email);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // 쿠키에서 "jwt" 토큰 추출
    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
