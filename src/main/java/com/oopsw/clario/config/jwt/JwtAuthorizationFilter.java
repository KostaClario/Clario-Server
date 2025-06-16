package com.oopsw.clario.config.jwt;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;

    // 비활성 사용자 허용 URI 목록
    private static final List<String> ALLOWED_INACTIVE_URIS = List.of("/api/member/join");

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info(">>> JwtAuthorizationFilter 진입: {}", request.getRequestURI());

        String token = extractToken(request);
        if (token == null || !jwtUtil.isValid(token)) {
            log.warn("유효하지 않은 토큰 또는 Authorization 헤더 없음");
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.extractEmail(token);
        if (email == null) {
            log.warn("토큰에서 이메일 파싱 실패");
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.info("이미 인증된 요청");
            filterChain.doFilter(request, response);
            return;
        }

        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            log.warn("DB에서 사용자 조회 실패");
            filterChain.doFilter(request, response);
            return;
        }

        // 비활성 사용자는 특정 요청만 허용
        if (!Boolean.TRUE.equals(member.getActivation()) &&
                !ALLOWED_INACTIVE_URIS.contains(request.getRequestURI())) {
            log.warn("비활성 사용자 접근 차단: {}", email);
            filterChain.doFilter(request, response);
            return;
        }

        setAuthentication(request, member);
        filterChain.doFilter(request, response);
    }

    // prefix + header  JwtProperties 로부터 추출
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(jwtProperties.getHeader()); // e.g. "Authorization"
        String prefix = jwtProperties.getPrefix();                   // e.g. "Bearer"
        if (header != null && header.startsWith(prefix + " ")) {
            return header.substring((prefix + " ").length());
        }
        return null;
    }


    // 인증 설정 로직 함수로 추출하여 중복 제거
    private void setAuthentication(HttpServletRequest request, Member member) {
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

        log.info("인증 객체 설정 완료: {}", member.getEmail());
    }
}
