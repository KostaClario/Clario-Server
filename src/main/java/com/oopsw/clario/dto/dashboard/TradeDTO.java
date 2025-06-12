package com.oopsw.clario.dto.dashboard;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class TradeDTO {
    private Integer memberId;
    private String cardStoreName;
    private Long cardTradeMoney;
    private String accountSource;
    private Long accountTradeMoney;
    private String cardTradeMonth;
    private String accountTradeMonth;
}
