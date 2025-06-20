package com.oopsw.clario.controller;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.MonthlyDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import com.oopsw.clario.service.DashboardService;
import com.oopsw.clario.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private AuthUtil authUtil;

    // ✅ 1. 월별 수입 조회 - GET
    @GetMapping("/monthly-income")
    public ResponseEntity<Long> getMonthlyIncome(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String yearDate,
            @RequestParam String monthDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .yearDate(yearDate)
                .monthDate(monthDate)
                .build();
        return ResponseEntity.ok(dashboardService.getMonthlyIncome(dto));
    }

    // ✅ 2. 월별 지출 조회 - GET
    @GetMapping("/monthly-expense")
    public ResponseEntity<Long> getMonthlyExpense(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String yearDate,
            @RequestParam String monthDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .yearDate(yearDate)
                .monthDate(monthDate)
                .build();

        return ResponseEntity.ok(dashboardService.getMonthlyExpense(dto));
    }

    // ✅ 3. 월별 수입 대비 지출 비율 - GET
    @GetMapping("/monthly-ratio")
    public ResponseEntity<MonthlyDTO> getMonthlyRatio(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String yearDate,
            @RequestParam String monthDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .yearDate(yearDate)
                .monthDate(monthDate)
                .build();

        return ResponseEntity.ok(dashboardService.getIncomeSpending(dto));
    }

    // ✅ 4. 오늘 지출 - GET
    @GetMapping("/today-expense")
    public ResponseEntity<List<TradeDTO>> getTodayExpense(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String todayDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .todayDate(todayDate)
                .build();

        return ResponseEntity.ok(dashboardService.getTodayExpense(dto));
    }

    // ✅ 5. 오늘 수입 - GET
    @GetMapping("/today-income")
    public ResponseEntity<List<TradeDTO>> getTodayIncome(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String todayDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .todayDate(todayDate)
                .build();

        return ResponseEntity.ok(dashboardService.getTodayIncome(dto));
    }

    // ✅ 6. 상위 3개 지출 카테고리 - GET
    @GetMapping("/top3-category")
    public ResponseEntity<List<Top3CategoryDTO>> getTop3Category(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String yearDate,
            @RequestParam String monthDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .yearDate(yearDate)
                .monthDate(monthDate)
                .build();

        return ResponseEntity.ok(dashboardService.getTop3Category(dto));
    }

    // ✅ 7. 연간 지출 - GET
    @GetMapping("/year-expenses")
    public ResponseEntity<List<TradeDTO>> getYearsExpenses(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String yearDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .yearDate(yearDate)
                .build();

        return ResponseEntity.ok(dashboardService.getYearsExpenses(dto));
    }

    // ✅ 8. 연간 수입 - GET
    @GetMapping("/year-incomes")
    public ResponseEntity<List<TradeDTO>> getYearsIncomes(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam String yearDate) {

        MemberDateDTO dto = MemberDateDTO.builder()
                .memberId(authUtil.extractMemberId(user))
                .yearDate(yearDate)
                .build();

        return ResponseEntity.ok(dashboardService.getYearsIncomes(dto));
    }

    // ✅ 9. 목표 자산 조회 - GET
    @GetMapping("/target-assets")
    public ResponseEntity<Long> getTargetAssets(@AuthenticationPrincipal CustomOAuth2User user) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(dashboardService.getTargetAssets(memberId));
    }

    // ✅ 10. 총 자산 조회 - GET
    @GetMapping("/total-assets")
    public ResponseEntity<Long> getTotalAssets(@AuthenticationPrincipal CustomOAuth2User user) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(dashboardService.getTotalAssets(memberId));
    }

    // ✅ 11. 목표 자산 설정 - POST
    @PostMapping("/target-assets")
    public ResponseEntity<Boolean> addTargetAssets(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam Long targetAssets) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", authUtil.extractMemberId(user));
        map.put("targetAssets", targetAssets);

        return ResponseEntity.ok(dashboardService.addTargetAssets(map));
    }
}