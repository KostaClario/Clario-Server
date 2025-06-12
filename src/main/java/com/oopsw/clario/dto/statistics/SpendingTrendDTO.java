package com.oopsw.clario.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SpendingTrendDTO {
    private int changeRate;
    private String icon;
    private String summary;
}
