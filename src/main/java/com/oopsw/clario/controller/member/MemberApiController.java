package com.oopsw.clario.controller.member;


import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
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
