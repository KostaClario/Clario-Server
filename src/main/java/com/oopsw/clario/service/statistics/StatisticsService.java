package com.oopsw.clario.service.statistics;

import com.oopsw.clario.dto.statistics.*;
import com.oopsw.clario.repository.statistics.StatisticsRepository;
import com.oopsw.clario.util.ExpensePredictor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsRepository statisticsRepository;

//    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(Long memberId) {
//        List<MonthlyExpenseTotalDTO> result = statisticsRepository.getMonthlyExpenseTotal(memberId);
//        if (result == null || result.isEmpty()) {
//            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
//            return Collections.emptyList();
//        }
//        return result;
//    }

    public List<MonthlyExpenseTotalDTO> getMonthlyExpenseTotal(Integer memberId, Long year, Long month) {
        List<MonthlyExpenseTotalDTO> result = statisticsRepository.getMonthlyExpenseTotal(memberId, year, month);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + ", year=" + year + ", month=" + month + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }


    public List<Top3CategoriesDTO> getTop3Categories(Integer memberId, Long year, Long month) {
        List<Top3CategoriesDTO> result = statisticsRepository.getTop3Categories(memberId, year, month);

        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + ", year=" + year + ", month=" + month + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }

        return result;
    }


    public List<MonthlyCardTradeTotalDTO> getMonthlyCardTradeTotal(Integer memberId, Long year) {
        List<MonthlyCardTradeTotalDTO> result = statisticsRepository.getMonthlyCardTradeTotal(memberId, year);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }

    public List<YearlyExpenseDTO> getYearlyTotalExpense(Integer memberId) {
        List<YearlyExpenseDTO> result = statisticsRepository.getYearlyTotalExpense(memberId);
        if (result == null || result.isEmpty()) {
            System.out.println("⚠ memberId=" + memberId + " 에 대한 연간 소비 내역이 없습니다.");
            return Collections.emptyList();
        }
        return result;
    }

    public List<YearlyIncomeDTO> getYearlyTotalIncome(Integer memberId) {
        List<YearlyIncomeDTO> cardList = statisticsRepository.getYearlyIncomeFromCard(memberId);
        List<YearlyIncomeDTO> accountList = statisticsRepository.getYearlyIncomeFromAccount(memberId);

        Map<Integer, Long> yearToTotalIncome = new HashMap<>();

        for (YearlyIncomeDTO dto : cardList) {
            yearToTotalIncome.merge(dto.getYear(), dto.getTotalIncome(), Long::sum);
        }
        for (YearlyIncomeDTO dto : accountList) {
            yearToTotalIncome.merge(dto.getYear(), dto.getTotalIncome(), Long::sum);
        }

        return yearToTotalIncome.entrySet().stream()
                .map(entry -> new YearlyIncomeDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(YearlyIncomeDTO::getYear))
                .collect(Collectors.toList());
    }

    public List<MonthlyIncomeAverageDTO> getMonthlyAverageIncome(Integer memberId) {
        return statisticsRepository.getMonthlyAverageIncome(memberId);
    }

    public List<MonthlyExpenseAverageDTO> getMonthlyExpenseAverage(Integer memberId) {
        return statisticsRepository.getMonthlyExpenseAverage(memberId);
    }

    public MonthlyIncomeDTO getMonthlyIncome(Integer memberId, Long year, Long month) {
        return statisticsRepository.getMonthlyIncome(memberId, year, month);
    }

    public CategoryStatisticsDTO getCategoryStatistics(Integer memberId, Long year, Long month) {
        List<TopCategoryByCountDTO> countBased = statisticsRepository.getTopCategoriesByCount(memberId, year, month);
        List<TopCategoryByAmountDTO> amountBased = statisticsRepository.getTopCategoriesByAmount(memberId, year, month);

        Long totalAmount = statisticsRepository.getTotalSpendAmount(memberId, year, month);
        Integer totalCount = statisticsRepository.getTotalSpendCount(memberId, year, month);
        Long previousMonthAmount = statisticsRepository.getTotalSpendAmount(
                memberId,
                month == 1 ? year - 1 : year,
                month == 1 ? 12 : month - 1

        );
        Long userIncome = statisticsRepository.getTotalIncome(memberId, year, month);

        CategoryStatisticsDTO dto = new CategoryStatisticsDTO(amountBased, totalAmount, totalCount, userIncome, previousMonthAmount);
        dto.setCountBased(countBased); // count 기반 추가

        return dto;
    }


    public CategoryStatisticsDTO getTopCategoriesByAmount(Integer memberId, Long year, Long month) {
        List<TopCategoryByAmountDTO> topCategories = statisticsRepository.getTopCategoriesByAmount(memberId, year, month);

        Long totalAmount = statisticsRepository.getTotalSpendAmount(memberId, year, month);
        Integer totalCount = statisticsRepository.getTotalSpendCount(memberId, year, month);

        Long previousMonthAmount = statisticsRepository.getTotalSpendAmount(memberId,
                month == 1 ? year - 1 : year,
                month == 1 ? 12 : month - 1);

        Long userIncome = statisticsRepository.getTotalIncome(memberId, year, month); // 없으면 null 반환

        return new CategoryStatisticsDTO(topCategories, totalAmount, totalCount, userIncome, previousMonthAmount);
    }

    public List<TopCategoryByAmountDTO> getTopCategoriesByAmount1(Integer memberId, Long year, Long month) {
        return statisticsRepository.getTopCategoriesByAmount(memberId, year, month);
    }


    public Long getMonthlyExpenseSum(Integer memberId, Long year, Long month) {
        return statisticsRepository.getMonthlyExpenseSum(memberId, year, month);
    }

    public Double getMonthlyExpenseGrowthRate(Integer memberId, Long year, Long month) {
        // 현재 월 출금 합계
        Long current = statisticsRepository.getMonthlyExpenseSum(memberId, year, month);
        if (current == null) current = 0L;

        // 전월 계산
        Long previousMonth = month - 1;
        Long previousYear = year;
        if (previousMonth == 0) {
            previousMonth = 12L;
            previousYear = year - 1;
        }

        Long previous = statisticsRepository.getMonthlyExpenseSum(memberId, previousYear, previousMonth);
        if (previous == null) previous = 0L;

        // 증감률 계산
        if (previous == 0L) {
            return current > 0L ? 100.0 : 0.0;
        }

        double growthRate = ((double) (current - previous) / previous) * 100.0;
        return Math.round(growthRate * 100.0) / 100.0;
    }

    public MonthlyExpenseComparisonDTO getMonthlyExpenseComparison(Integer memberId, Long year, Long month) {
        Long current = statisticsRepository.getMonthlyExpense(memberId, year, month);
        if (current == null) current = 0L;

        Long previousMonth = month - 1;
        Long previousYear = year;
        if (previousMonth == 0) {
            previousMonth = 12L;
            previousYear -= 1;
        }

        Long previous = statisticsRepository.getMonthlyExpense(memberId, previousYear, previousMonth);
        if (previous == null) previous = 0L;

        double rate;
        if (previous == 0L) {
            rate = current > 0L ? 100.0 : 0.0;
        } else {
            rate = ((double)(current - previous) / previous) * 100.0;
        }

        // 반올림
        rate = Math.round(rate * 100.0) / 100.0;

        return new MonthlyExpenseComparisonDTO(current, previous, rate);
    }

    public IncomeVsExpenseDTO getIncomeVsExpense(Integer memberId, Long year, Long month) {
        Long income = statisticsRepository.getMonthlyIncomeSum(memberId, year, month);
        Long expense = statisticsRepository.getMonthlyExpense(memberId, year, month);

        double rate = (income == 0) ? 0.0 : (double) expense / income * 100.0;
        rate = Math.round(rate * 100.0) / 100.0;

        IncomeVsExpenseDTO dto = new IncomeVsExpenseDTO();
        dto.setIncome(income);
        dto.setExpense(expense);
        dto.setRate(rate);
        return dto;
    }

    public long predictNextMonthExpense(Integer memberId) {
        List<Long> expenses = statisticsRepository.getMonthlyTotalExpenses(memberId);
        if (expenses == null || expenses.size() < 2) {
            throw new IllegalStateException("소비 예측을 위한 월별 데이터가 부족합니다.");
        }

        return ExpensePredictor.predictNextMonth(expenses);
    }

    public SpendingTrendDTO analyzeSpendingTrend(Integer memberId, Long year, Long month) {
        Long prevYear = month == 1 ? year - 1 : year;
        Long prevMonth = month == 1 ? 12 : month - 1;

        long current = statisticsRepository.getMonthlyExpenseSum(memberId, year, month);
        long previous = statisticsRepository.getMonthlyExpenseSum(memberId, prevYear, prevMonth);

        if (previous == 0) {
            return new SpendingTrendDTO(0, "➖", "비교할 데이터가 없어 소비 추세를 분석할 수 없습니다.");
        }

        int rate = (int) Math.round((double)(current - previous) / previous * 100);

        String icon;
        String summary;

        if (rate <= -50) {
            icon = "🔻";
            summary = "전월 대비 소비가 크게 감소했습니다. 효과적인 절약 패턴이 나타나고 있으며, 특히 변동비 항목에서 뚜렷한 감소세를 보이고 있습니다. 우수한 가계 관리 상태입니다.";
        } else if (rate < 0) {
            icon = "🔻";
            summary = "전월 대비 소비가 소폭 감소했습니다. 절약 의지가 반영된 안정적인 소비가 유지되고 있습니다.";
        } else if (rate == 0) {
            icon = "➖";
            summary = "전월과 동일한 수준의 지출을 유지하고 있습니다. 소비 패턴이 매우 일정합니다.";
        } else if (rate <= 30) {
            icon = "🔺";
            summary = "전월 대비 소비가 소폭 증가했습니다. 고정비 비율이 소폭 증가했을 수 있습니다.";
        } else {
            icon = "🔺";
            summary = "전월 대비 소비가 크게 증가했습니다. 변동비 사용이 급격히 늘어난 것으로 보입니다. 소비 관리가 필요합니다.";
        }

        return new SpendingTrendDTO(rate, icon, summary);
    }

}



