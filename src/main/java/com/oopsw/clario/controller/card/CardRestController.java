package com.oopsw.clario.controller.card;

import com.oopsw.clario.config.auth.CustomOAuth2User;
import com.oopsw.clario.dto.card.AllCardsDTO;
import com.oopsw.clario.dto.card.CardFilterRequestDTO;
import com.oopsw.clario.dto.statistics.CategoryStatisticsDTO;
import com.oopsw.clario.dto.statistics.MonthlyExpenseComparisonDTO;
import com.oopsw.clario.dto.statistics.TopCategoryByAmountDTO;
import com.oopsw.clario.service.card.CardService;
import com.oopsw.clario.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
public class CardRestController {
    private final CardService cardService;
    private final AuthUtil authUtil;

    @GetMapping("/all") //모든 카드 조회
    public ResponseEntity<List<AllCardsDTO>> getAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

//    @PostMapping("/filter") //상위 카테고리 리스트 받아서 필터링된 카드 반환
//    public ResponseEntity<List<AllCardsDTO>> getCardsByFilter(
//            @RequestBody List<String> parentNames) {
//        return ResponseEntity.ok(cardService.getCardsByParentCategories(parentNames));
//    }
    @PostMapping("/filter")
    public ResponseEntity<List<AllCardsDTO>> getCardsByFilter(@RequestBody CardFilterRequestDTO filter) {
    List<AllCardsDTO> cards = cardService.getCardsByParentCategoriesAndType(
            filter.getParentCategories(),
            filter.getCardType()
    );
    return ResponseEntity.ok(cards);
    }

    @PostMapping("/recommend")
    public ResponseEntity<List<AllCardsDTO>> getRecommendedCards(@RequestBody CardFilterRequestDTO filter) {
        List<AllCardsDTO> cards = cardService.getCardsByParentCategoriesAndType(
                filter.getParentCategories(),
                filter.getCardType()
        );
        return ResponseEntity.ok(cards);
    }

//    @GetMapping("/top-category-stats")
//    public ResponseEntity<CategoryStatisticsDTO> getCategoryStatistics(@AuthenticationPrincipal CustomOAuth2User user,
//                                                                       @RequestParam Long year,
//                                                                       @RequestParam Long month) {
//        Integer memberId = authUtil.extractMemberId(user);
//        return ResponseEntity.ok(cardService.getCategoryStatistics(memberId, year, month));
//    }

    @GetMapping("/top-category-stats")
    public ResponseEntity<CategoryStatisticsDTO> getCategoryStatistics(@AuthenticationPrincipal CustomOAuth2User user,
                                                                       @RequestParam Long year,
                                                                       @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(cardService.getCategoryStatistics(memberId, year, month));
    }


    @GetMapping("/top-category-stats-amount")
    public ResponseEntity<List<TopCategoryByAmountDTO>> getCategoryStatistics2(@AuthenticationPrincipal CustomOAuth2User user,
                                                                              @RequestParam Long year,
                                                                              @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(cardService.getTopCategoriesByAmount(memberId, year, month));
    }

    @GetMapping("/monthly-expense")
    public ResponseEntity<Long> getMonthlyExpenseSum(@AuthenticationPrincipal CustomOAuth2User user,
                                                     @RequestParam Long year,
                                                     @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        Long totalExpense = cardService.getMonthlyExpenseSum(memberId, year, month);
        return ResponseEntity.ok(totalExpense);
    }

    @GetMapping("/expense/growth")
    public ResponseEntity<MonthlyExpenseComparisonDTO> getMonthlyExpenseGrowth(
            @AuthenticationPrincipal CustomOAuth2User user,
            @RequestParam Long year,
            @RequestParam Long month) {
        Integer memberId = authUtil.extractMemberId(user);
        return ResponseEntity.ok(cardService.getMonthlyExpenseComparison(memberId, year, month));
    }

    @PostMapping("/recommend/grouped")
    public ResponseEntity<Map<String, List<AllCardsDTO>>> getGroupedCardRecommendations(@RequestBody CardFilterRequestDTO filter) {
        Map<String, List<AllCardsDTO>> groupedCards = cardService.getGroupedRecommendedCards(
                filter.getParentCategories(),
                filter.getCardType()
        );
        return ResponseEntity.ok(groupedCards);
    }




}
