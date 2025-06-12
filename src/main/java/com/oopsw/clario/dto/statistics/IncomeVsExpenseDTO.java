package com.oopsw.clario.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IncomeVsExpenseDTO {
    private Long income;
    private Long expense;
    private Double rate; // (지출 / 수입) * 100
}