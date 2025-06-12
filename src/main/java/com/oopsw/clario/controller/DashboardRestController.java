package com.oopsw.clario.controller;

import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.MonthlyDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import com.oopsw.clario.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

    @Autowired
    private DashboardService dashboardService;

    @PostMapping("/monthly-income")
    public ResponseEntity<Long> getMonthlyIncome(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getMonthlyIncome(memberDateDTO));
    }

    @PostMapping("/monthly-expense")
    public ResponseEntity<Long> getMonthlyExpense(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getMonthlyExpense(memberDateDTO));
    }

    @PostMapping("/monthly-ratio")
    public ResponseEntity<MonthlyDTO> getMonthlyRatio(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getIncomeSpending(memberDateDTO));
    }

    @GetMapping("/target-assets/{memberId}")
    public ResponseEntity<Long> getTargetAssets(@PathVariable Integer memberId) {
        return ResponseEntity.ok(dashboardService.getTargetAssets(memberId));
    }

    @GetMapping("/total-assets/{memberId}")
    public ResponseEntity<Long> getTotalAssets(@PathVariable Integer memberId) {
        return ResponseEntity.ok(dashboardService.getTotalAssets(memberId));
    }

    @PostMapping("/target-assets")
    public ResponseEntity<Boolean> addTargetAssets(@RequestBody HashMap<String, Object> map) {
        return ResponseEntity.ok(dashboardService.addTargetAssets(map));
    }

    @PostMapping("/today-expense")
    public ResponseEntity<List<TradeDTO>> getTodayExpense(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getTodayExpense(memberDateDTO));
    }

    @PostMapping("/today-income")
    public ResponseEntity<List<TradeDTO>> getTodayIncome(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getTodayIncome(memberDateDTO));
    }

    @PostMapping("/top3-category")
    public ResponseEntity<List<Top3CategoryDTO>> getTop3Category(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getTop3Category(memberDateDTO));
    }

    @PostMapping("/year-expenses")
    public ResponseEntity<List<TradeDTO>> getYearsExpenses(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getYearsExpenses(memberDateDTO));
    }

    @PostMapping("/year-incomes")
    public ResponseEntity<List<TradeDTO>> getYearsIncomes(@RequestBody MemberDateDTO memberDateDTO) {
        return ResponseEntity.ok(dashboardService.getYearsIncomes(memberDateDTO));
    }
}
