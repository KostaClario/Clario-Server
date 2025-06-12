package com.oopsw.clario.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CardDetailDTO {
    private int cardTradeId;
    private String cardName;
    private String cardType;
    private String cardNum;
    private String cardDay;
    private String cancelDay;
    private Long cardMoney;
    private String cardStoreName;
    private String industry;
    private String businessNum;
    private String categoryName;
}
