package com.oopsw.clario.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MonthlyExpenseComparisonDTO {
    private Long currentExpense;   // 이번 달 지출 합계
    private Long previousExpense;  // 지난 달 지출 합계
    private Double growthRate;     // 증감률 (%)
}