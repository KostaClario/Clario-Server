package com.oopsw.clario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCardDTO {
    private String cardNum;
    private String cardName;
    private String cardCompany;
    private String cardType;
    private String cardBank;
    private String cardBankAccount;
}