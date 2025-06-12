package com.oopsw.clario.controller.member;


import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.exception.SaveFailedException;
import com.oopsw.clario.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

    private final MemberService memberService;

    @ModelAttribute
    public void addCommonAttributes(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        if (user != null) {
            String email = user.getEmail();
            Member member = memberService.findNullableMemberByEmail(email);
            if (member != null) {
                model.addAttribute("name", member.getName());
                model.addAttribute("email", member.getEmail());
                model.addAttribute("profile", user.getPhoto());
            } else {
                System.out.println("등록되지 않은 사용자: " + email);
                // 안전한 기본값 설정 (선택)
                model.addAttribute("name", "이름");
                model.addAttribute("email", email);
                model.addAttribute("profile", user.getPhoto());
            }
        }

    }

}

