package com.oopsw.clario.config.auth;



import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        (csrfConfig) -> csrfConfig.disable()
                )
                .authorizeHttpRequests(
                        (auth) -> auth
                                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/account-css/**").permitAll()

                                // 로그인 및 회원가입 관련
                                .requestMatchers("/html/account/login.html").permitAll()
                                .requestMatchers("/privacy", "/agree", "/join").permitAll()
                                .requestMatchers(HttpMethod.POST, "/join").permitAll()

                                // OAuth2 관련
                                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                                // 계좌 카드 연동
                                .requestMatchers("/mydata/**").authenticated()

                                // 통계
                                .requestMatchers("/api/**", "/statistics", "/card", "/dashboard").authenticated()

                                // 인증 필요한 기능
                                .requestMatchers("/account/edit", "/account/remove",
                                        "/account/reset-password", "/account/verify-password").authenticated()

                                .anyRequest().authenticated()
                )
                .logout(
                        (logoutConfig) -> logoutConfig
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/html/account/login.html")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredSessionStrategy(event -> {
                            event.getResponse().sendRedirect("/loginView?error=session");
                        })
                )
                .oauth2Login(
                        (oauth2) -> oauth2
                                .loginPage("/html/account/login.html")
                                .failureHandler(customOAuth2FailureHandler)
                                .userInfoEndpoint(
                                        (userInfo) -> userInfo
                                                .userService(customOAuth2UserService)
                                )
                                // redirectUrl 세션 기반 분기 처리
                                .successHandler(customOAuth2SuccessHandler)
                )
        ;
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 세션 레지스트리 등록
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // 로그아웃/세션 만료 시 SessionRegistry에서 제거되도록 이벤트 퍼블리셔 등록
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}



