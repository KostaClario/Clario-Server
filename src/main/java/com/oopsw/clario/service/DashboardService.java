package com.oopsw.clario.service;

import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.MonthlyDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import com.oopsw.clario.repository.DashboardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
@Slf4j
@Service
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    public Long getMonthlyIncome(MemberDateDTO memberDateDTO) {
        Long income = dashboardRepository.getMonthlyIncome(memberDateDTO);
        return (income == null) ? 0L : income;
    }

    public Long getMonthlyExpense(MemberDateDTO memberDateDTO) {
        Long expense = dashboardRepository.getMonthlyExpense(memberDateDTO);
        return (expense == null) ? 0L : expense;
    }

    public MonthlyDTO getIncomeSpending(MemberDateDTO memberDateDTO) {
        Long income = dashboardRepository.getMonthlyIncome(memberDateDTO);
        Long expense = dashboardRepository.getMonthlyExpense(memberDateDTO);

        income = income != null ? income : 0L;
        expense = expense != null ? expense : 0L;

        float percentage = 0f;

        if (income > 0) {
            percentage = Math.round(((float) expense / income) * 100 * 100) / 100f;
        }
        MonthlyDTO monthlyDTO = MonthlyDTO.builder().income(income).expense(expense).incomeSpending(percentage).build();

        return monthlyDTO;
    }

    public Long getTargetAssets(Integer memberId) {
        Long target = dashboardRepository.getTargetAssets(memberId);
        return (target == null || target < 0) ? 0L : target;
    }

    public Long getTotalAssets(Integer memberId) {
        Long total = dashboardRepository.getTotalAssets(memberId);
        return (total == null || total < 0) ? 0L : total;
    }

    public boolean addTargetAssets(HashMap<String, Object> map) {
        if (map.get("memberId") == null || map.get("targetAssets") == null) {
            throw new IllegalArgumentException("memberId 또는 targetAssets가 누락되었습니다.");
        }

        try {
            int memberId = Integer.parseInt(map.get("memberId").toString());
            long targetAssets = Long.parseLong(map.get("targetAssets").toString());

            if (memberId <= 0 || targetAssets <= 0) {
                throw new IllegalArgumentException("memberId와 targetAssets는 양수여야 합니다.");
            }

            HashMap<String, Object> result = new HashMap<>();
            result.put("memberId", memberId);
            result.put("targetAssets", targetAssets);

            return dashboardRepository.addTargetAssets(result);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("숫자 형식이 올바르지 않습니다.", e);
        }
    }


    public List<TradeDTO> getTodayExpense(MemberDateDTO memberDateDTO) {
        List<TradeDTO> list = dashboardRepository.getTodayExpense(memberDateDTO);
        return (list == null) ? List.of() : list;
    }

    public List<TradeDTO> getTodayIncome(MemberDateDTO memberDateDTO) {
        List<TradeDTO> list = dashboardRepository.getTodayIncome(memberDateDTO);
        return (list == null) ? List.of() : list;
    }

    public List<Top3CategoryDTO> getTop3Category(MemberDateDTO memberDateDTO) {
        List<Top3CategoryDTO> list = dashboardRepository.getTop3Category(memberDateDTO);
        if(list == null || list.isEmpty()) return List.of();
        return list.stream()
                .filter(dto -> dto.getCategoryMoney() != null && dto.getCategoryMoney() > 0)
                .toList();
    }

    public List<TradeDTO> getYearsExpenses(MemberDateDTO memberDateDTO) {
        List<TradeDTO> list = dashboardRepository.getYearsExpenses(memberDateDTO);
        if(list == null || list.isEmpty()) return List.of();
        return list;
    }

    public List<TradeDTO> getYearsIncomes(MemberDateDTO memberDateDTO) {
        List<TradeDTO> list =  dashboardRepository.getYearsIncomes(memberDateDTO);
        if(list == null || list.isEmpty()) return List.of();
        return list;
    }
}
