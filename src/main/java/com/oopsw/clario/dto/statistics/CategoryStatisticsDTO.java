package com.oopsw.clario.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryStatisticsDTO {
    private List<TopCategoryByCountDTO> countBased;

    private Long totalAmount;              // 총 소비 금액
    private Integer totalCount;            // 결제 횟수
    private Long userIncome;              // 사용자 수입 (nullable 허용)
    private Long previousMonthAmount;     // 전월 소비 총액

    private List<TopCategoryByAmountDTO> amountBased;

    public CategoryStatisticsDTO(List<TopCategoryByAmountDTO> amountBased,
                                 Long totalAmount,
                                 Integer totalCount,
                                 Long userIncome,
                                 Long previousMonthAmount) {
        this.amountBased = amountBased;
        this.totalAmount = totalAmount;
        this.totalCount = totalCount;
        this.userIncome = userIncome;
        this.previousMonthAmount = previousMonthAmount;
    }

}
