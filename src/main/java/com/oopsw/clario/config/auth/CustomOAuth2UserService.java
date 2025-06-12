package com.oopsw.clario.config.auth;

import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.MemberRepository;
import com.oopsw.clario.domain.member.Role;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2 사용자 서비스로부터 사용자 정보 로드
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate  = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);




        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 구글 응답을 추출 name email
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Optional<Member> optionalMember = memberRepository.findByEmail(attributes.getEmail());
        Member member = null;

        if(optionalMember.isPresent()) {
            Member existing = optionalMember.get();

            if(!existing.getActivation()){
                // 비활성된 회원 재가입
                httpSession.setAttribute("oauthAttributes", attributes);
                httpSession.setAttribute("reactivate", true);
                httpSession.setAttribute("redirectUrl", "/privacy");
            }else{
                // 기존 활성 회원 정보 업데이트 후 로그인
                member = existing;
                httpSession.setAttribute("redirectUrl", "/dashboard");
            }
        }else{
            // 신규 사용자
            httpSession.setAttribute("oauthAttributes", attributes);
            httpSession.setAttribute("redirectUrl", "/privacy");
        }

        log.info("CustomOAuth2UserService 실행됨");
        log.info("이메일: " + attributes.getEmail());
        log.info("멤버 여부: " + (member != null));
        log.info("세션 ID: " + httpSession.getId());

        // 사용자 권한
        String role = (member != null) ? member.getRoleKey() : Role.USER.getKey();

        // CustomOAuth2User 리턴
        return new CustomOAuth2User(
                attributes.getEmail(),
                attributes.getEmail(),
                attributes.getAttributes(),
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }
}