package com.oopsw.clario.controller.member;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.dto.member.UpdateMemberDTO;
import com.oopsw.clario.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/member")
@Slf4j
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/loginView")
    public ResponseEntity<String> loginView() {
        return ResponseEntity.ok("account/login");
    }

    @GetMapping("/remove")
    public ResponseEntity<String> removePage() {
        return ResponseEntity.ok("account/user-remove");
    }

    @PostMapping("/remove")
    public ResponseEntity<?> remove(@AuthenticationPrincipal CustomOAuth2User user,
                                    @RequestParam String password,
                                    HttpServletRequest request,
                                    HttpServletResponse response,
                                    Authentication authentication) {
        String email = user.getEmail();

        if (!memberService.checkPassword(email, password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
        }

        memberService.deactivateMember(email);

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return ResponseEntity.ok("탈퇴 처리 완료");
    }

    @GetMapping("/edit")
    public ResponseEntity<?> updateInfoView(@AuthenticationPrincipal CustomOAuth2User user) {
        String email = user.getEmail();
        Member member = memberService.getMemberByEmail(email);

        UpdateMemberDTO dto = new UpdateMemberDTO();
        dto.setEmail(email);
        dto.setName(member.getName());
        dto.setPhonenum(member.getPhonenum());

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> updateInfo(@AuthenticationPrincipal CustomOAuth2User user,
                                        @RequestBody UpdateMemberDTO dto) {
        String email = user.getEmail();

        if (dto.getNewPassword() != null && !dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        memberService.resetMemberInfo(email, dto);
        return ResponseEntity.ok("회원 정보 수정 완료");
    }

    @GetMapping("/loginSuccess")
    public ResponseEntity<String> loginSuccess(HttpSession session) {
        log.info("세션 ID (loginSuccess): " + session.getId());
        String redirect = (String) session.getAttribute("redirectUrl");
        log.info("redirectAfterLogin: " + redirect);
        return ResponseEntity.ok(redirect != null ? redirect : "/");
    }

    @GetMapping("/privacy")
    public ResponseEntity<?> privacy(HttpSession session) {
        OAuthAttributes attributes = (OAuthAttributes) session.getAttribute("oauthAttributes");
        if (attributes == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        return ResponseEntity.ok("account/privacy");
    }

    @GetMapping("/agree")
    public ResponseEntity<String> agree() {
        return ResponseEntity.ok("/html/account/join.html");
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UpdateMemberDTO dto,
                                  HttpSession session) {
        OAuthAttributes attributes = (OAuthAttributes) session.getAttribute("oauthAttributes");
        if (attributes == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String email = attributes.getEmail();

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다.");
        }

        Optional<Member> optional = memberService.findByEmail(email);

        if (optional.isEmpty()) {
            memberService.saveMember(email, dto.getName(), dto.getPhonenum(), dto.getNewPassword());
        } else {
            Member member = optional.get();
            if (!member.getActivation()) {
                memberService.reactivateMember(email, dto.getName(), dto.getPhonenum(), dto.getNewPassword());
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 활성화된 계정입니다.");
            }
        }

        session.removeAttribute("oauthAttributes");
        session.setAttribute("redirectAfterLogin", "mydata/mybankandcardlist");

        Map<String, String> response = new HashMap<>();
        response.put("message", "가입 성공");
        response.put("redirect", "/mydata/mybankandcardlist");

        return ResponseEntity.ok(response);
    }
}
