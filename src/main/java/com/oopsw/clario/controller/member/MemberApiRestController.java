package com.oopsw.clario.controller.member;


import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/api/userinfo")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal CustomOAuth2User user) {
        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("authenticated", false);
            return result;
        }

        String email = user.getEmail();
        Member member = memberService.findNullableMemberByEmail(email);

        if (member == null) {
            // 이 경우는 회원가입 안 됐거나 DB 문제이므로, 오류 처리하거나 강제 로그아웃 등 조치 필요
            throw new RuntimeException("회원 정보가 존재하지 않습니다. 로그인 세션을 재확인 해주세요.");
        }

        // 정상적으로 회원가입된 유저의 정보 반환
        result.put("authenticated", true);
        result.put("name", member.getName());
        result.put("email", member.getEmail());
        result.put("phonenum", member.getPhonenum());
        result.put("profile", user.getPhoto());

        return result;
    }

    @PostMapping("/reset-password")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@AuthenticationPrincipal CustomOAuth2User user,
                                           @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");
        String email = user.getEmail();

        if(!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        memberService.resetPassword(email, newPassword);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/verify-password")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@AuthenticationPrincipal CustomOAuth2User user,
                                            @RequestBody Map<String, String> request) {

        String password = request.get("password");
        String email = user.getEmail();

        boolean result = memberService.checkPassword(email, password);

        if(result) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 틀렸습니다.");
        }
    }
}
