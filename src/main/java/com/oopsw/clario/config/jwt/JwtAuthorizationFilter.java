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
import java.util.Map;

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
        log.info(">>> JwtAuthorizationFilter 진입: {}", request.getRequestURI());

        String token = extractTokenFromHeader(request);

        if (token == null) {
            log.warn("JWT 토큰이 Authorization 헤더에 없음");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("추출된 토큰: {}", token);

        if (!jwtUtil.validateToken(token)) {
            log.warn("JWT 토큰이 유효하지 않음");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.getUsername(token);
        log.info("토큰에서 파싱한 이메일: {}", email);

        if (email == null) {
            log.warn("이메일 파싱 실패");
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("이미 인증 객체 존재: {}", SecurityContextHolder.getContext().getAuthentication());
            filterChain.doFilter(request, response);
            return;
        }

        Member member = memberRepository.findByEmail(email).orElse(null);
        log.info("DB에서 조회된 사용자: {}", member);

        if (member == null) {
            log.warn("이메일로 사용자 조회 실패");
            filterChain.doFilter(request, response);
            return;
        }

        if (!Boolean.TRUE.equals(member.getActivation())) {
            if ("/api/member/join".equals(request.getRequestURI())) {
                log.info("비활성화 사용자지만 /api/member/join 요청 → 인증 객체 설정");

                CustomOAuth2User principal = new CustomOAuth2User(
                        member.getName(),
                        member.getEmail(),
                        Collections.emptyMap(),
                        Collections.singleton(() -> member.getRoleKey())
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.info("인증 객체 설정 완료 (/join 예외처리)");
                filterChain.doFilter(request, response);
                return;
            }

            log.warn("비활성화 사용자 접근 차단");
            filterChain.doFilter(request, response);
            return;
        }

        String picture = jwtUtil.getClaim(token, "picture");

        // 활성화된 사용자 인증 객체 설정
        CustomOAuth2User principal = new CustomOAuth2User(
                member.getName(),
                member.getEmail(),
                Map.of("picture", picture),
                Collections.singleton(() -> member.getRoleKey())
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("인증 객체 설정 완료");
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 JWT 추출
    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}
