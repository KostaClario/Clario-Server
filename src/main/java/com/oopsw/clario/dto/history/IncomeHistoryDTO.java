package com.oopsw.clario.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IncomeHistoryDTO {
    private String accountDay;
    private Long accountMoney;
    private String source;
    private String bankAccountName;
    private String bankName;
    private String date;
    private int memberId;
}
