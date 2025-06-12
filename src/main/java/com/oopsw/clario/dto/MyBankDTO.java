package com.oopsw.clario.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyBankDTO {
    private String bankAccountNum;
    private Long balance;
    private String bankAccountName;
    private String bankName;
}