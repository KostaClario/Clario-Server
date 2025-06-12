package com.oopsw.clario.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IncomeDTO {
    private Integer memberId;
    private String accountDay;
    private String accountName;
    private String source;
    private Long accountMoney;
    private String accountType;
    private String bankName;
    private String bankAccountName;
    private String bankAccountNum;
    private int CategoryId;
}
