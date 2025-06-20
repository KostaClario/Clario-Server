package com.oopsw.clario.controller.history;


import com.oopsw.clario.config.auth.CustomOAuth2User;

import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.dto.history.*;
import com.oopsw.clario.service.MemberService;
import com.oopsw.clario.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RestControllerAdvice
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HistoryRestController {

    private final HistoryService historyService;
    private final MemberService memberService;

    @GetMapping("/account")
    public List<AccountDTO> accountList(@AuthenticationPrincipal CustomOAuth2User user) {
        String email = user.getEmail();
        Member member = memberService.getMemberByEmail(email);
        Integer memberId = member.getMemberId();

        return historyService.accountList(memberId);
    }

    @GetMapping("/card")
    public List<CardDTO> cardList(@AuthenticationPrincipal CustomOAuth2User user) {
        String email = user.getEmail();
        Member member = memberService.getMemberByEmail(email);
        Integer memberId = member.getMemberId();

        return historyService.cardList(memberId);
    }

    @GetMapping("/cardDetail/{cardTradeId}")
    public CardDetailDTO cardDetail(@PathVariable int cardTradeId) {
        System.out.println("🟡 전달된 거래 ID: " + cardTradeId); // ✅ 찍어보기
        return historyService.cardDetail(cardTradeId);
    }

    @GetMapping("/incomeHistory")
    public List<IncomeHistoryDTO> incomeHistory(@AuthenticationPrincipal CustomOAuth2User user,
                                                @RequestParam(required = false) String date) {
        Integer memberId = memberService.getMemberByEmail(user.getEmail()).getMemberId();
        System.out.println("컨트롤러");
        return historyService.incomeHistory(memberId, date);
    }

    @GetMapping("/expenseHistory")
    public List<ExpenseHistoryDTO> expenseHistory(@AuthenticationPrincipal CustomOAuth2User user,
                                                  @RequestParam (required = false) String date,
                                                  @RequestParam (required = false) String category,
                                                  @RequestParam (required = false) String card) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", getMemberId(user));
        params.put("date", date);
        params.put("category", category);
        params.put("card", card);
        return historyService.expenseHistory(params);
    }
    private Integer getMemberId(CustomOAuth2User user) {
        String email = user.getEmail();
        Member member = memberService.getMemberByEmail(email);
        return member.getMemberId();
    }
    @PostMapping("/income")
    public ResponseEntity<String> income(@RequestBody IncomeDTO dto) {
        int result = historyService.income(dto);
        System.out.println("받은 DTO: " + dto);
        if (result == 1) {
            return ResponseEntity.ok("입금 성공");
        } else {
            return ResponseEntity.status(500).body("입금 실패");
        }
    }

    @PostMapping("/expense")
    public ResponseEntity<ExpenseDTO> expense(@RequestBody ExpenseDTO dto) {
        System.out.println("✅ 컨트롤러 도착: " + dto);

        try {
            int result = historyService.expense(dto);
            System.out.println("🟢 저장 결과: " + result);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("❌ 저장 중 예외 발생");
            e.printStackTrace(); // 이게 IntelliJ 콘솔에 나옴
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/category")
    public List<Map<String, Object>> categoryList() {

        return historyService.categoryList();
    }

}
