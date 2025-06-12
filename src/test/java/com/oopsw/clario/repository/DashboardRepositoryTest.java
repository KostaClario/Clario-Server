package com.oopsw.clario.repository;

import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DashboardRepositoryTest {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Test
    public void getMonthlyIncome_True() {
        MemberDateDTO memberDateDTO = MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("4").build();
        System.out.println(dashboardRepository.getMonthlyIncome(memberDateDTO));
    }


    //@Test
    public void getMonthlyIncome_False() {
        MemberDateDTO memberDateDTO = MemberDateDTO.builder().memberId(1).build();
        System.out.println(dashboardRepository.getMonthlyIncome(memberDateDTO));
    }


    @Test
    public void getMonthlyExpense_True() {
        MemberDateDTO memberDateDTO = MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("4").build();
        System.out.println(dashboardRepository.getMonthlyExpense(memberDateDTO));
    }


    @Test
    public void getTargetAssets_True() {
        System.out.println(dashboardRepository.getTargetAssets(1));
    }


    @Test
    public void getTotalAssets_True() {
        System.out.println(dashboardRepository.getTotalAssets(1));
    }

    @Test
    public void addTargetAssets_True() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", 1);
        map.put("targetAssets", 30000000);
        System.out.println(dashboardRepository.addTargetAssets(map));
    }

    @Test
    public void addTargetAssets_False() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("memberId", -1);
        map.put("targetAssets", "90000000");
        System.out.println(dashboardRepository.addTargetAssets(map));
    }


    @Test
    public void getTodayExpense_True(){
        List<TradeDTO> today = dashboardRepository.getTodayExpense(MemberDateDTO.builder().memberId(1).todayDate("2024-05-06").build());
        for(TradeDTO todayTradeDTO : today){
            TradeDTO day = TradeDTO.builder().memberId(1).cardStoreName(todayTradeDTO.getCardStoreName()).cardTradeMoney(todayTradeDTO.getCardTradeMoney()).build();
            System.out.println(day);
        }
    }

    @Test
    public void getTodayIncome_True(){
        List<TradeDTO> today = dashboardRepository.getTodayIncome(MemberDateDTO.builder().memberId(1).todayDate("2024-05-06").build());
        for(TradeDTO todayTradeDTO : today){
            TradeDTO day = TradeDTO.builder().memberId(1).accountSource(todayTradeDTO.getAccountSource()).accountTradeMoney(todayTradeDTO.getAccountTradeMoney()).build();
            System.out.println(day);

        }

    }

    @Test
    public void getTop3Category_True(){
        List<Top3CategoryDTO> categories = dashboardRepository.getTop3Category(MemberDateDTO.builder().memberId(1).yearDate("2024").monthDate("5").build());
        for(Top3CategoryDTO top3CategoryDTO : categories){
            Top3CategoryDTO category = Top3CategoryDTO.builder().memberId(1).categoryName(top3CategoryDTO.getCategoryName()).categoryMoney(top3CategoryDTO.getCategoryMoney()).build();
            System.out.println(category);
        }
    }

    @Test
    public void getYearsExpenses_True(){
        List<TradeDTO> yearsExpense = dashboardRepository.getYearsExpenses(MemberDateDTO.builder().memberId(1).yearDate("2024").build());
        for(TradeDTO tradeDTO : yearsExpense){
            TradeDTO years = TradeDTO.builder().memberId(1).cardTradeMonth(tradeDTO.getCardTradeMonth()).cardTradeMoney(tradeDTO.getCardTradeMoney()).build();
            System.out.println(years);
        }
    }

    @Test
    public void getYearsIncomes_True(){
        List<TradeDTO> yearsIncomes= dashboardRepository.getYearsIncomes(MemberDateDTO.builder().memberId(1).yearDate("2024").build());
        for(TradeDTO tradeDTO : yearsIncomes){
            TradeDTO years = TradeDTO.builder().memberId(1).accountTradeMonth(tradeDTO.getAccountTradeMonth()).accountTradeMoney(tradeDTO.getAccountTradeMoney()).build();
            System.out.println(years);
        }
    }
}
