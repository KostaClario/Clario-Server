package com.oopsw.clario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MydataResponseDTO {
    private List<MyBankDTO> banks;
    private List<MyCardDTO> cards;

    private Integer memberId;
    private String name;
}