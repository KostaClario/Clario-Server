package com.oopsw.clario.repository.card;

import com.oopsw.clario.dto.card.AllCardsDTO;
import com.oopsw.clario.dto.card.CardFilterRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CardRepositoryTests {
    @Autowired
    CardRepository cardRepository;

    @Test
    public void getAllCards() {
        List<AllCardsDTO> result = cardRepository.getAllCards();
        if (result.size() > 0) {
            System.out.println(result);
        } else {
            System.out.println("카드 정보 없음!");
        }
    }
//
//    @Test
//    public void testGetCardsByCategoryNames() {
//        List<String> subCategories = Arrays.asList("카페", "쇼핑", "편의점");
//
//        List<AllCardsDTO> result = cardRepository.getCardsByCategoryNames(subCategories);
//
//        if (result.isEmpty()) {
//            System.out.println("추천 결과 없음");
//        } else {
//            result.forEach(System.out::println);
//        }
//    }

    @Test
    public void testGetCardsByCategoryNamesAndType() {
        List<String> subCategories = Arrays.asList("카페", "소핑", "편의점");
        String cardType = "신용";

        List<AllCardsDTO> result = cardRepository.getCardsByCategoryNamesAndType(subCategories, cardType);

        if (result.isEmpty()) {
            System.out.println("추천 결과 없음");
        } else {
            result.forEach(System.out::println);
        }
    }

        }


