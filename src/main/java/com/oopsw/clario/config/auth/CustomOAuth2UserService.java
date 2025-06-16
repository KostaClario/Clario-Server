package com.oopsw.clario.config.auth;

import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import com.oopsw.clario.domain.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        String email = attributes.getEmail();

        Member member = memberRepository.findByEmail(email).orElse(null);

        String role = (member != null && Boolean.TRUE.equals(member.getActivation()))
                ? member.getRoleKey()
                : Role.USER.getKey();  // 기본 권한

        log.info("OAuth 로그인 유저: email={}, 기존 회원 여부={}, 활성 상태={}", email, member != null, member != null && member.getActivation());

        return new CustomOAuth2User(
                email,
                email,
                attributes.getAttributes(),
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }
}
