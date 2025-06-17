package com.oopsw.clario.controller.member;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class BarController {

    @Autowired
    MemberService memberService;

    @GetMapping(value = "/bar", produces = "application/json")
    public ResponseEntity<?> barInfo(@AuthenticationPrincipal CustomOAuth2User user) {

        if (user == null) {
            log.warn("인증 객체 없음 - user가 null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        }

        String email = user.getEmail();
        log.info("🔑 인증된 사용자 이메일: {}", email);

        Member member = memberService.getMemberByEmail(email);
        if (member == null) {
            log.warn("DB에 해당 이메일의 사용자 없음: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        String name = member.getName();
        String photo = user.getPhoto();

        log.info("name: {}, photo: {}", name, photo);

        return ResponseEntity.ok(Map.of(
                "name", name,
                "photo", photo
        ));
    }
}
