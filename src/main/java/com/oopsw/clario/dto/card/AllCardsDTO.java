package com.oopsw.clario.dto.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllCardsDTO {
    private Long cardRecommendationId; // 카드추천 ID
    private String cardCompany;        // 카드사
    private String cardName;           // 카드명
    private String benefitSummary;     // 혜택 요약
    private String cardUrl;            // 카드 URL
    private String cardImage;          // 카드 이미지
    private String benefitCategory;    // 혜택 카테고리
    private String cardType;           // 카드 타입
    private String parentCategory;
}
