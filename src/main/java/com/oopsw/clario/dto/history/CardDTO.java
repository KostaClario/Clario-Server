package com.oopsw.clario.dto.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CardDTO {
    @JsonProperty("card_id")
    private String cardId;

    @JsonProperty("card_name")
    private String cardName;

    @JsonProperty("card_num_masked")
    private String cardNumMasked;

    @JsonProperty("card_type")
    private String cardType;

    @JsonProperty("issuer_code")
    private String issuerCode;

    @JsonProperty("issuer_name")
    private String issuerName;

    @JsonProperty("annual_fee")
    private String annualFee;

    @JsonProperty("card_status")
    private String cardStatus;

    @JsonProperty("linked_account")
    private LinkedAccount linkedAccount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class LinkedAccount {
        @JsonProperty("account_num_masked")
        private String accountNumMasked;

        @JsonProperty("bank_code_std")
        private String bankCodeStd;

        @JsonProperty("bank_name")
        private String bankName;
    }
}
