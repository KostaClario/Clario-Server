package com.oopsw.clario.util;

import com.oopsw.clario.dto.card.AllCardsDTO;
import java.util.*;

public class CategoryMapper {

    private static final Map<String, List<String>> CATEGORY_MAP = new HashMap<>();
    private static final Map<String, String> REVERSE_MAP = new HashMap<>();

    static {
        CATEGORY_MAP.put("식비", Arrays.asList("편의점", "카페", "패스트푸드", "한식", "중식", "일식", "마트", "배달앱", "제과점"));
        CATEGORY_MAP.put("교통비", Arrays.asList("버스", "지하철", "택시", "기차", "고속버스", "자가용 주유", "톨게이트", "대리운전"));
        CATEGORY_MAP.put("여가비", Arrays.asList("영화", "음악", "도서", "콘서트", "전시회", "넷플릭스", "게임", "유튜브 프리미엄"));
        CATEGORY_MAP.put("고정비", Arrays.asList("월세", "관리비", "통신비", "인터넷", "보험료", "학자금 대출", "구독료"));
        CATEGORY_MAP.put("유흥비", Arrays.asList("술집", "클럽", "노래방", "룸카페", "마사지", "호텔", "성인용품"));
        CATEGORY_MAP.put("건강비", Arrays.asList("병원", "약국", "건강보조식품", "헬스장", "요가", "필라테스", "한의원"));
        CATEGORY_MAP.put("생활비", Arrays.asList("세탁", "청소용품", "화장지", "주방용품", "욕실용품", "전기요금", "수도요금"));
        CATEGORY_MAP.put("기타", Arrays.asList("선물", "기부", "예비비", "분실물", "기타"));

        // 역매핑 초기화
        for (Map.Entry<String, List<String>> entry : CATEGORY_MAP.entrySet()) {
            String parent = entry.getKey();
            for (String child : entry.getValue()) {
                REVERSE_MAP.put(child, parent);
            }
        }
    }

    // 상위 → 하위
    public static List<String> getChildCategories(List<String> parentNames) {
        List<String> result = new ArrayList<>();
        for (String parent : parentNames) {
            List<String> children = CATEGORY_MAP.get(parent);
            if (children != null) {
                result.addAll(children);
            }
        }
        return result;
    }

    // 하위 → 상위
    public static String getParentCategory(String childName) {
        return REVERSE_MAP.getOrDefault(childName, "기타");
    }

    // 카드 리스트를 상위 카테고리별로 그룹핑
    public static Map<String, List<AllCardsDTO>> groupCardsByParentCategory(List<AllCardsDTO> cards) {
        Map<String, List<AllCardsDTO>> result = new HashMap<>();
        for (AllCardsDTO card : cards) {
            String childCategory = card.getBenefitCategory(); // DB에서 받아온 하위 카테고리
            String parent = getParentCategory(childCategory);
            result.computeIfAbsent(parent, k -> new ArrayList<>()).add(card);
        }
        return result;
    }
}
