package com.oopsw.clario.dto.dashboard;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class MonthlyDTO {
    private Long income;
    private Long expense;
    private float incomeSpending;
}
