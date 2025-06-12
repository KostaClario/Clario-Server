package com.oopsw.clario.domain.member;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER","사용자"),
    GUEST("ROLE_GUEST","실패");

    private final String key;
    private final String title;
}
