package com.oopsw.clario.controller.statistics;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.domain.member.Member;
import com.oopsw.clario.dto.statistics.*;
import com.oopsw.clario.dto.statistics.MonthlyExpenseComparisonDTO;
import com.oopsw.clario.service.MemberService;
import com.oopsw.clario.service.statistics.StatisticsService;
import com.oopsw.clario.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsRestController {
    private final StatisticsService statisticsService;
    private final MemberService memberService;
    private final AuthUtil authUtil;

    @GetMapping("/monthly-expense")
    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(
            @AuthenticationPrincipal CustomOAuth2User user, //@AuthenticationPrincipal >> Oauth 세션 받아옴
            @RequestParam Long year,
            @RequestParam Long month) {

        Integer memberId = authUtil.extractMemberId(user);

        return statisticsService.getMonthlyExpenseTotal(memberId, year, month); // ⚠️ 3개 전달
    }

    @GetMapping("/monthly-income")
    public ResponseEntity<MonthlyIncomeDTO> getMonthlyIncome(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam Long year,
            @RequestParam Long month) {

        Integer memberId = authUtil.extractMemberId(user);

        MonthlyIncomeDTO result = statisticsService.getMonthlyIncome(memberId, year, month);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/top3")
    public List<Top3CategoriesDTO> getTop3Categories(@AuthenticationPrincipal CustomOAuth2User user,
                                                     @RequestParam Long year,
                                                     @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        return statisticsService.getTop3Categories(memberId, year, month);
    }


    @GetMapping("/monthly-card-trade")
    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam Long year) {  // year 파라미터 추가
        Integer memberId = authUtil.extractMemberId(user);
        return statisticsService.getMonthlyCardTradeTotal(memberId, year);
    }

    @GetMapping("/yearly-total-expense")
    public ResponseEntity<List<YearlyExpenseDTO>> getYearlyTotalExpense( @AuthenticationPrincipal CustomOAuth2User user) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(statisticsService.getYearlyTotalExpense(memberId));
    }

    @GetMapping("/yearly-income")
    public List<YearlyIncomeDTO> getYearlyIncome(@AuthenticationPrincipal CustomOAuth2User user) {
        Integer memberId = authUtil.extractMemberId(user);
        return statisticsService.getYearlyTotalIncome(memberId);
    }

    @GetMapping("/monthly-income-average")
    public ResponseEntity<List<MonthlyIncomeAverageDTO>> getMonthlyAverageIncome(@AuthenticationPrincipal CustomOAuth2User user) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(statisticsService.getMonthlyAverageIncome(memberId));
    }

    @GetMapping("/monthly-expense-average")
    public List<MonthlyExpenseAverageDTO> getMonthlyExpenseAverage(@AuthenticationPrincipal CustomOAuth2User user) {
        Integer memberId = authUtil.extractMemberId(user);
        return statisticsService.getMonthlyExpenseAverage(memberId);
    }

    @GetMapping("/top-category-stats")
    public ResponseEntity<CategoryStatisticsDTO> getTopCategoryStats(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam Long year,
            @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(statisticsService.getCategoryStatistics(memberId, year, month));
    }


    @GetMapping("/expense/growth")
    public ResponseEntity<MonthlyExpenseComparisonDTO> getMonthlyExpenseGrowth(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam Long year,
            @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(statisticsService.getMonthlyExpenseComparison(memberId, year, month));
    }

    @GetMapping("/income-vs-expense")
    public ResponseEntity<IncomeVsExpenseDTO> getIncomeVsExpense(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam Long year,
            @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(statisticsService.getIncomeVsExpense(memberId, year, month));
    }

    @GetMapping("/next-month-prediction")
    public ResponseEntity<Long> getNextMonthPrediction(@AuthenticationPrincipal CustomOAuth2User user) {
        Integer memberId = authUtil.extractMemberId(user);
        long prediction = statisticsService.predictNextMonthExpense(memberId);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping("/spending-trend")
    public ResponseEntity<SpendingTrendDTO> getSpendingTrend(@AuthenticationPrincipal CustomOAuth2User user,
                                                             @RequestParam Long year,
                                                             @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        SpendingTrendDTO trend = statisticsService.analyzeSpendingTrend(memberId, year, month);
        return ResponseEntity.ok(trend);
    }



}

