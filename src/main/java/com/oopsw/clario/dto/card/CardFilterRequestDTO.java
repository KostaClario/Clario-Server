package com.oopsw.clario.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CardFilterRequestDTO {
    private List<String> parentCategories = new ArrayList<>();
    private String cardType =""; // 신용 또는 체크, NullExcep 방지 위해 기본값 설정
}
