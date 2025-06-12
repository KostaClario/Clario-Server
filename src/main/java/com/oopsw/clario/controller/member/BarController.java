package com.oopsw.clario.controller.member;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BarController {
    private final MemberService memberService;

    public BarController(MemberService memberService) {
        this.memberService = memberService;
    }
    @GetMapping("bar")
    public ResponseEntity<?> barInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        Member member = memberService.getMemberByEmail(user.getEmail());
        return ResponseEntity.ok(member);

    }
}
