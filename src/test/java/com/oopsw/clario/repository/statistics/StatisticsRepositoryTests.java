package com.oopsw.clario.repository.statistics;

import com.oopsw.clario.dto.statistics.MonthlyCardTradeTotalDTO;
import com.oopsw.clario.dto.statistics.MonthlyExpenseTotalDTO;
import com.oopsw.clario.dto.statistics.Top3CategoriesDTO;
import com.oopsw.clario.dto.statistics.YearlyExpenseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class StatisticsRepositoryTests {
    @Autowired
    private StatisticsRepository statisticsRepository;

//    @Test
//    public void testMonthlyExpenseTotal() {
//        Long memberId = Long.valueOf(1L);
//        List<MonthlyExpenseTotalDTO> result = statisticsRepository.getMonthlyExpenseTotal(memberId);
//        if (result.size() > 0) {
//            System.out.println(result);
//        } else {
//            System.out.println("memberId값 잘못됨!");
//        }
//    }

//    //@Test
//    public void testTop3Categories() {
//        Long memberId = Long.valueOf(1L);
//        List<Top3CategoriesDTO> result = statisticsRepository.getTop3Categories(memberId);
//        if (result.size() > 0) {
//            System.out.println(result);
//        } else {
//            System.out.println("memberId값 잘못됨!");
//        }
//    }
//
//   // @Test
//    public void testMonthlyCardTradeTotal() {
//        Long memberId = Long.valueOf(1L);
//        List<MonthlyCardTradeTotalDTO> result = statisticsRepository.getMonthlyCardTradeTotal(memberId);
//        if (result.size() > 0) {
//            System.out.println(result);
//        } else {
//            System.out.println("memberId값 잘못됨!");
//        }
//
//    }
//
//    @Test
//    public void testYearlyTotalExpense() {
//        Long memberId = Long.valueOf(4L);
//        List<YearlyExpenseDTO> result = statisticsRepository.getYearlyTotalExpense(memberId);
//        if (result.size() > 0) {
//            System.out.println(result);
//        } else {
//            System.out.println("memberId값 잘못됨!");
//        }
//    }
}