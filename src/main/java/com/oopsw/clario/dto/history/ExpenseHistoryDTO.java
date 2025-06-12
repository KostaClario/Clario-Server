package com.oopsw.clario.dto.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExpenseHistoryDTO {
    private String cardDay;         // 거래일자
    private String cardStoreName;   // 가맹점 이름
    private Long cardMoney;         // 결제 금액
    private String businessNum;     // 사업자 번호
    private String categoryName;    // 카테고리 이름
    private String cardName;        // 카드 이름
    private int cardTradeId;
    private int memberId;
    private String date;
}
