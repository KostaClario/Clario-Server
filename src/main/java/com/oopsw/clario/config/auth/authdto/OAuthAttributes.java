package com.oopsw.clario.config.auth.authdto;

import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.domain.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.io.Serializable;
import java.util.Map;

@Getter
@Slf4j
public class OAuthAttributes implements Serializable {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    private final String name;
    private final String email;
    private final String oauth;

    // 회원가입 시 추가로 입력받을 항목 (이 값들은 로그인 시점엔 null일 수 있음)
    private final String phonenum;
    private final String password;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String name,
                           String email,
                           String oauth,
                           String phonenum,
                           String password) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.oauth = oauth;
        this.phonenum = phonenum;
        this.password = password;
    }

    // 최초 사용자 생성 시 사용
    // 하지만 추가정보를 받기때문에 우리 프로젝트에선 사용하지않음.
    // 나중 공부를 위해 남겨놓겠슴.
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .oauth(oauth)
                .phonenum(phonenum != null ? phonenum : "") // null 방지
                .password(password != null ? password : "") // null 방지
                .totalAssets(0L)
                .targetAssets(0L)
                .activation(false) // 회원가입 폼에서 최종 등록 시 true
                .lastSyncedAt(null)
                .role(Role.USER)
                .build();
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {

        String email = (String) attributes.get("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("구글 로그인 결과에 이메일이 없음");
        }

        log.info("email: " + attributes.get("email"));
        return OAuthAttributes.builder()
                .name(null)
                .email(email)
                .oauth("google")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .phonenum(null)   // 나중에 회원가입 폼에서 입력받음
                .password(null)   // 나중에 회원가입 폼에서 입력받음
                .build();
    }
}
