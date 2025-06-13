package com.oopsw.clario.controller;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import com.oopsw.clario.dto.MydataResponseDTO;
import com.oopsw.clario.service.MemberService;
import com.oopsw.clario.service.MyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mydata")
public class MyDataRestController {

    @Autowired
    private MyDataService myDataService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/mydataconnection")
    public ResponseEntity<MydataResponseDTO> getMyDataConnection(
            @AuthenticationPrincipal CustomOAuth2User user) {

        Optional<Member> member = memberService.findByEmail(user.getEmail());
        Integer memberId = member.get().getMemberId();

        List<MyBankDTO> banks = myDataService.getMyBankConnection(memberId);
        List<MyCardDTO> cards = myDataService.getMyCardConnection(memberId);

        MydataResponseDTO response = new MydataResponseDTO();
        response.setBanks(banks);
        response.setCards(cards);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/mybankandcardlist")
    public ResponseEntity<MydataResponseDTO> getMyBankAndCardList(
            @AuthenticationPrincipal CustomOAuth2User user) {
        Optional<Member> member = memberService.findByEmail(user.getEmail());
        Integer memberId = member.get().getMemberId();

        List<MyBankDTO> banks = myDataService.getMyBankList(memberId);
        List<MyCardDTO> cards = myDataService.getMyCardList(memberId);

        MydataResponseDTO response = new MydataResponseDTO();
        response.setBanks(banks);
        response.setCards(cards);
        response.setMemberId(memberId);
        response.setName(member.get().getName());

        return ResponseEntity.ok(response);
    }
}
