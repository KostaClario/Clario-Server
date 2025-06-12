package com.oopsw.clario.controller.member;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.config.auth.authdto.OAuthAttributes;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.dto.UpdateMemberDTO;
import com.oopsw.clario.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Slf4j
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/loginView";
    }

    @GetMapping("/account/remove")
    public String remove() {
        return "account/user-remove";
    }

    @PostMapping("/account/remove")
    public String remove(@AuthenticationPrincipal CustomOAuth2User user,
                         @RequestParam String password,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         Authentication authentication,
                         Model model) {

        String email = user.getEmail();

        if(!memberService.checkPassword(email, password)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "account/user-remove";
        }

        memberService.deactivateMember(email);

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/loginView";
    }

    @GetMapping("/account/edit")
    public String updateInfoView(Model model,@AuthenticationPrincipal CustomOAuth2User user) {
        String email = user.getEmail();

        Member member = memberService.getMemberByEmail(email);

        //프래그먼트용 속성들(model)에 추가
        model.addAttribute("memberId", member.getMemberId());
        model.addAttribute("name", member.getName());
        model.addAttribute("user", user);

        UpdateMemberDTO dto = new UpdateMemberDTO();
        dto.setEmail(email);
        dto.setName(member.getName());
        dto.setPhonenum(member.getPhonenum());

        model.addAttribute("updateForm", dto);

        return "account/user-info-edit";
    }

    @PostMapping("/account/edit")
    public String updateInfo(@ModelAttribute("updateForm") UpdateMemberDTO dto,
                             @AuthenticationPrincipal CustomOAuth2User user,
                             Model model) {
        String email = user.getEmail();

        if(dto.getNewPassword() != null && !dto.getNewPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "account/user-info-edit";
        }

        memberService.resetMemberInfo(email,dto);
        return "redirect:/dashboard";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session) {

        log.info("세션 ID (loginSuccess): " + session.getId());

        String redirect = (String) session.getAttribute("redirectUrl");

        log.info("redirectAfterLogin: " + redirect);

        return "redirect:" + (redirect != null ? redirect : "/");
    }

    @GetMapping("/loginView")
    public String loginView() {
        return "account/login";
    }

    @GetMapping("/privacy")
    public String privacy(HttpSession session) {
        OAuthAttributes attributes = (OAuthAttributes) session.getAttribute("oauthAttributes");
        if (attributes == null) {
            return "redirect:/loginView";
        }
        return "account/privacy";
    }

    @GetMapping("/agree")
    public String agree() {
        return "account/join";
    }

    @PostMapping("/join")
    public String join(@RequestParam String name,
                       @RequestParam String phonenum,
                       @RequestParam String password,
                       @RequestParam String confirmPassword,
                       HttpSession session,
                       Model model) {
        OAuthAttributes attributes = (OAuthAttributes) session.getAttribute("oauthAttributes");
        if(attributes == null) {
            return "redirect:/loginView";
        }

        String email = attributes.getEmail();

        if(!password.equals(confirmPassword)){
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("email", email);
            return "account/join";
        }
        Optional<Member> optional = memberService.findByEmail(email);

        if (optional.isEmpty()) {
            // 신규 가입
            memberService.saveMember(email, name, phonenum, password);
        } else {
            Member member = optional.get();
            if (!member.getActivation()) {
                // ✅ 재가입 처리
                memberService.reactivateMember(email, name, phonenum, password);
            } else {
                // 이미 가입된 사용자
                model.addAttribute("error", "이미 활성화된 계정입니다.");
                return "account/join";
            }
        }

            session.removeAttribute("oauthAttributes");
            session.setAttribute("redirectAfterLogin", "mydata/mybankandcardlist");

            return "redirect:/mydata/mybankandcardlist";
        }
    }