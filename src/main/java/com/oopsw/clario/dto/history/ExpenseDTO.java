package com.oopsw.clario.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExpenseDTO {
    private String cardDay;
    private Long cardMoney;
    private String cardStoreName;
    private String businessNum;
    private String industry;
    private String cardType;
    private String categoryName;
    private String cardName;
    private String cardNum;
}
