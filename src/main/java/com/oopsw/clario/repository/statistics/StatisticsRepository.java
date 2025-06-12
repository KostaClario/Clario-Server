package com.oopsw.clario.repository.statistics;

import com.oopsw.clario.dto.statistics.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Mapper
public interface StatisticsRepository {
    // ✅ 이미 올바름 - 월별 특정 데이터
    List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(@Param("memberId") Integer memberId,
                                                        @Param("year") Long year,
                                                        @Param("month") Long month);

    // ✅ 이미 올바름 - Top3 카테고리
    List<Top3CategoriesDTO> getTop3Categories(@Param("memberId") Integer memberId,
                                              @Param("year") Long year,
                                              @Param("month") Long month);

    // 🔄 수정 필요 - year 파라미터 추가
    List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(@Param("memberId") Integer memberId,
                                                            @Param("year") Long year);

    // ✅ 현재 구조 유지 - 연간 데이터
    List<YearlyExpenseDTO> getYearlyTotalExpense(@Param("memberId") Integer memberId);

    List<YearlyIncomeDTO> getYearlyIncomeFromCard(@Param("memberId") Integer memberId);

    List<YearlyIncomeDTO> getYearlyIncomeFromAccount(@Param("memberId") Integer memberId);

    // ✅ 현재 구조 유지 - 평균 데이터
    List<MonthlyIncomeAverageDTO> getMonthlyAverageIncome(@Param("memberId") Integer memberId);

    List<MonthlyExpenseAverageDTO> getMonthlyExpenseAverage(@Param("memberId") Integer memberId);

    MonthlyIncomeDTO getMonthlyIncome(@Param("memberId") Integer memberId,
                                      @Param("year") Long year,
                                      @Param("month") Long month);

    List<TopCategoryByCountDTO> getTopCategoriesByCount(@Param("memberId") Integer memberId,
                                                        @Param("year") Long year,
                                                        @Param("month") Long month);

    List<TopCategoryByAmountDTO> getTopCategoriesByAmount(@Param("memberId") Integer memberId,
                                                          @Param("year") Long year,
                                                          @Param("month") Long month);

    Long getMonthlyExpenseSum(@Param("memberId") Integer memberId,
                              @Param("year") Long year,
                              @Param("month") Long month);


    Long getMonthlyIncomeSum(@Param("memberId") Integer memberId,
                             @Param("year") Long year,
                             @Param("month") Long month);

    List<Long> getMonthlyTotalExpenses(@Param("memberId") Integer memberId);

    Long getMonthlyExpense(@Param("memberId") Integer memberId,
                              @Param("year") Long year,
                              @Param("month") Long month);
    Long getTotalSpendAmount(@Param("memberId") Integer memberId,
                             @Param("year") Long year,
                             @Param("month") Long month);

    Integer getTotalSpendCount(@Param("memberId") Integer memberId,
                               @Param("year") Long year,
                               @Param("month") Long month);

    Long getTotalIncome(@Param("memberId") Integer memberId,
                        @Param("year") Long year,
                        @Param("month") Long month);



}


