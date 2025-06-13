package com.oopsw.clario.controller.member;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BarController {
    @Autowired
    MemberService memberService;

    @GetMapping("/bar")
    public ResponseEntity<?> barInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        String email = user.getEmail();

        // DB에서 회원 정보 조회
        Member member = memberService.getMemberByEmail(email);

        return ResponseEntity.ok(Map.of(
                "name", member.getName(),
                "photo", user.getPhoto()
        ));
    }
}