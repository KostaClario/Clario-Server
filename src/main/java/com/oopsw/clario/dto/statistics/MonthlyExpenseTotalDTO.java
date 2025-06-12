package com.oopsw.clario.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MonthlyExpenseTotalDTO {
    private Integer memberId;
    private String name;
    private String tradeMonth;
    private int categoryId;
    private String categoryName;
    private Long totalSpending;
}
