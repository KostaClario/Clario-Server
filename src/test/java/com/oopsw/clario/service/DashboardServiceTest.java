package com.oopsw.clario.service;

import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.MonthlyDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class DashboardServiceTest {
    @Autowired
    public DashboardService dashboardService;

    @Test
    public void getMonthlyIncome_True(){
        MemberDateDTO memberDateDTO = MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("04").build();
        Long monthlyIncome = dashboardService.getMonthlyIncome(memberDateDTO);
        assertEquals(6634860, monthlyIncome);
    }

    //@Test
    public void getMonthlyIncome_False(){
        MemberDateDTO memberDateDTO = MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("04").build();
        Long monthlyIncome = dashboardService.getMonthlyIncome(memberDateDTO);
        assertEquals(6634860, monthlyIncome);
    }

    @Test
    public void getMonthlyExpense_True(){
        MemberDateDTO memberDateDTO = MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("04").build();
        Long monthlyExpense = dashboardService.getMonthlyExpense(memberDateDTO);
        assertEquals(2863260, monthlyExpense);
    }

    @Test
    public void getIncomeSpending_True(){
        MemberDateDTO memberDateDTO = MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("04").build();
        MonthlyDTO incomeSpending = dashboardService.getIncomeSpending(memberDateDTO);
        System.out.println(incomeSpending);
    }

    @Test
    public void getTargetAssets_True(){
        Long result = dashboardService.getTargetAssets(1);
        assertEquals(30000000,result);
    }


    @Test
    public void getTotalAssets_True(){
        Long result = dashboardService.getTotalAssets(1);
        assertEquals(3300000,result);
    }

    @Test
    public void addTargetAssets_True(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId",1);
        map.put("targetAssets",95000000);
        boolean result = dashboardService.addTargetAssets(map);
        assertNotEquals(true, result);

    }

    @Test
    public void addTargetAssets_False(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId",-1);
        map.put("targetAssets",500000000);
        boolean result = dashboardService.addTargetAssets(map);
        assertEquals(true, result);

    }

    @Test
    public void getTodayExpense_True(){
        List<TradeDTO> result = dashboardService.getTodayExpense(MemberDateDTO.builder().memberId(1).todayDate("2024-05-29").build());
        System.out.println(result.size());

    }

    @Test
    public void getTodayIncome_True(){
        List<TradeDTO> result = dashboardService.getTodayIncome(MemberDateDTO.builder().memberId(1).todayDate("2024-05-29").build());
        System.out.println(result.size());

    }

    @Test
    public void getTop3Category_True(){
        List<Top3CategoryDTO> categories = dashboardService.getTop3Category(MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("5").build());
        assertEquals(3, categories.size());
        System.out.println(categories);
    }

    @Test
    public void getYearsExpenses_True(){
        List<TradeDTO> list = dashboardService.getYearsExpenses(MemberDateDTO.builder().memberId(1).yearDate("2024").build());
        System.out.println(list);
    }

    @Test
    public void getYearsIncomes_True(){
        List<TradeDTO> list = dashboardService.getYearsIncomes(MemberDateDTO.builder().memberId(1).yearDate("2024").build());
        System.out.println(list);
    }
}
