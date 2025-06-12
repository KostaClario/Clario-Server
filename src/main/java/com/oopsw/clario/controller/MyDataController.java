package com.oopsw.clario.controller;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import com.oopsw.clario.service.MemberService;
import com.oopsw.clario.service.MyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mydata")
public class MyDataController {

    @Autowired
    private MyDataService myDataService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/mydataconnection")
    public String myDataConnection(Model model, @AuthenticationPrincipal CustomOAuth2User user) {
        String email = user.getEmail();

        Member member = memberService.getMemberByEmail(email);
        Integer memberId = member.getMemberId();

        List<MyBankDTO> banks = myDataService.getMyBankConnection(memberId);
        List<MyCardDTO> cards = myDataService.getMyCardConnection(memberId);

        model.addAttribute("banks", banks);
        model.addAttribute("cards", cards);

        return "mydata/mydataconnection";
    }

    @GetMapping("/mybankandcardlist")
    public String myBankAndCardList(Model model, @AuthenticationPrincipal CustomOAuth2User user) {

        String email = user.getEmail();
        Member member = memberService.getMemberByEmail(email);
        Integer memberId = member.getMemberId();

        List<MyBankDTO> banks = myDataService.getMyBankList(memberId);
        List<MyCardDTO> cards = myDataService.getMyCardList(memberId);
        model.addAttribute("banks", banks);
        model.addAttribute("cards", cards);
        model.addAttribute("pageTitle", "자산 조회");
        model.addAttribute("memberId", memberId);
        model.addAttribute("name", member.getName());
        model.addAttribute("user", user);
        model.addAttribute("contentFragment", "mydata/mybankandcardlist :: content");

        return "mydata/mybankandcardlist";
    }
}
