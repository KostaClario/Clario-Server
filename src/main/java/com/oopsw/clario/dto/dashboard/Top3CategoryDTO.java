package com.oopsw.clario.dto.dashboard;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class Top3CategoryDTO {
    private Integer memberId;
    private String categoryName;
    private Long categoryMoney;
}
