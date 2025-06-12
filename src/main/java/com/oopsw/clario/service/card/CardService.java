package com.oopsw.clario.service.card;

import com.oopsw.clario.dto.card.AllCardsDTO;
import com.oopsw.clario.dto.statistics.CategoryStatisticsDTO;
import com.oopsw.clario.dto.statistics.MonthlyExpenseComparisonDTO;
import com.oopsw.clario.dto.statistics.MonthlyIncomeDTO;
import com.oopsw.clario.dto.statistics.TopCategoryByAmountDTO;
import com.oopsw.clario.repository.card.CardRepository;
import com.oopsw.clario.service.statistics.StatisticsService;
import com.oopsw.clario.util.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    private final StatisticsService statisticsService;

    public List<AllCardsDTO> getAllCards() {
        List<AllCardsDTO> result = cardRepository.getAllCards();
        return result;
    }

//    public List<AllCardsDTO> getCardsByParentCategories(List<String> parentCategories) {
//        List<String> subCategories = CategoryMapper.getChildCategories(parentCategories);
//        return cardRepository.getCardsByCategoryNames(subCategories);
//    }

    public List<AllCardsDTO> getCardsByParentCategoriesAndType(List<String> parentCategories, String cardType) {
        List<String> subCategories = new ArrayList<>();
        if (parentCategories != null && !parentCategories.isEmpty()) {
            subCategories = CategoryMapper.getChildCategories(parentCategories);
        }
        return cardRepository.getCardsByCategoryNamesAndType(subCategories, cardType);
    }

    public CategoryStatisticsDTO getCategoryStatistics(Integer memberId, Long year, Long month) {
        return statisticsService.getCategoryStatistics(memberId, year, month);
    }

    public Long getMonthlyExpenseSum(Integer memberId, Long year, Long month) {
        return statisticsService.getMonthlyExpenseSum(memberId, year, month);
    }

    public MonthlyExpenseComparisonDTO getMonthlyExpenseComparison(Integer memberId, Long year, Long month) {
        return statisticsService.getMonthlyExpenseComparison(memberId, year, month);
    }

    public Map<String, List<AllCardsDTO>> getGroupedRecommendedCards(List<String> parentCategories, String cardType) {
        // 1. 모든 하위 카테고리 추출
        List<String> allChildCategories = CategoryMapper.getChildCategories(parentCategories);

        // 2. 한 번의 쿼리로 전체 카드 조회
        List<AllCardsDTO> allCards = cardRepository.getCardsByCategoryNamesAndType(allChildCategories, cardType);

        // 3. 상위 카테고리 기준으로 그룹핑
        Map<String, List<AllCardsDTO>> grouped = CategoryMapper.groupCardsByParentCategory(allCards);

        // 4. 각 그룹에서 최대 3개만 반환
        Map<String, List<AllCardsDTO>> result = new LinkedHashMap<>();
        for (String parent : parentCategories) {
            List<AllCardsDTO> cards = grouped.getOrDefault(parent, Collections.emptyList());
            result.put(parent, cards.stream().limit(3).collect(Collectors.toList()));
        }

        return result;
    }


    public List<TopCategoryByAmountDTO> getTopCategoriesByAmount(Integer memberId, Long year, Long month) {
        return statisticsService.getTopCategoriesByAmount1(memberId, year, month);
    }

    public MonthlyIncomeDTO getMonthlyIncome(Integer memberId, Long year, Long month){
        return statisticsService.getMonthlyIncome(memberId, year, month);
    }








}
