package com.oopsw.clario.dto.history;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountDTO {
    @JsonProperty("fintech_use_num")
    private String fintechUseNum;

    @JsonProperty("account_num_masked")
    private String bankAccountNum;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("account_status")
    private String accountStatus;

    @JsonProperty("bank_code_std")
    private String bankCodeStd;

    @JsonProperty("bank_name")
    private String bankName;

    @JsonProperty("account_name")
    private String accountName;
}
