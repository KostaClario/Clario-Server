package com.oopsw.clario.config.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final String name;
    private final String email;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(String name, String email, Map<String, Object> attributes,
                            Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.email = email;
        this.attributes = attributes;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return name != null ? name : email; // fallback 보장
    }

    public String getPhoto() {
        Object pic = attributes.get("picture");
        return pic != null ? pic.toString() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomOAuth2User)) return false;
        CustomOAuth2User that = (CustomOAuth2User) o;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

}
