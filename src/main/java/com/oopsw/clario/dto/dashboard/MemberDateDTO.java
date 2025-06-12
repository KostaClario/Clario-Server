package com.oopsw.clario.dto.dashboard;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class MemberDateDTO {
    private Integer memberId;
    private String yearDate;
    private String monthDate;
    private String todayDate;
}