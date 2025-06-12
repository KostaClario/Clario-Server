package com.oopsw.clario.repository.card;

import com.oopsw.clario.dto.card.AllCardsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CardRepository {
    List<AllCardsDTO> getAllCards();
    List<AllCardsDTO> getRecommendingCards(Map<String, Object> paramMap);
    //List<AllCardsDTO> getCardsByCategoryNames(List<String> categoryNames);
    List<AllCardsDTO> getCardsByCategoryNamesAndType(@Param("categories") List<String> categories,
                                                     @Param("cardType") String cardType);


}
