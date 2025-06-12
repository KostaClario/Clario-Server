package com.oopsw.clario.util;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final MemberService memberService;

    public Integer extractMemberId(CustomOAuth2User user) {
        String email = user.getEmail();
        Member member = memberService.getMemberByEmail(email);
        return member.getMemberId();
    }
}
