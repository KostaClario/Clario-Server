package com.oopsw.clario.controller;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class dashboardController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/statistics")
    public String statisticsView() {
        return "statistics/statisticsView";
    }

    @GetMapping("/card")
    public String cardsView() {
        return "card/cardView";
    }

    @GetMapping("/history")
    public String history() {
        return "history/history";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        String email = user.getEmail();
        Member member = memberService.getMemberByEmail(email);
        Integer memberId = member.getMemberId();

        model.addAttribute("memberId", memberId);
        model.addAttribute("name", member.getName());
//        model.addAttribute("user", user);

        return "dashboard/dashboard";
    }
}
