package com.oopsw.clario.service.card;

import com.oopsw.clario.dto.card.AllCardsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CardServiceTests {
    @Autowired
    private CardService cardService;

    @Test
    public void findAllCardsByParentCategories() {
        List<AllCardsDTO> result = cardService.getAllCards();
        System.out.println(result);
    }

//    @Test
//    public void testGetCardsByParentCategories() {
//        //List<String> parentCategories = Arrays.asList("식비", "교통비");
//        List<String> parentCategories = Arrays.asList("식비");
//        List<AllCardsDTO> result = cardService.getCardsByParentCategories(parentCategories);
//        System.out.println(result);
//    }

    @Test
    public void getCardsByParentCategoriesAndType() {
        List<String> parentCategories = Arrays.asList("식비", "교통비");
        String cardType = "신용";
        List<AllCardsDTO> result = cardService.getCardsByParentCategoriesAndType(parentCategories, cardType);
        System.out.println(result);
    }

}
