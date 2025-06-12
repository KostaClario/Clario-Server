package com.oopsw.clario.repository;

import com.oopsw.clario.dto.history.ExpenseDTO;
import com.oopsw.clario.dto.history.ExpenseHistoryDTO;
import com.oopsw.clario.dto.history.IncomeHistoryDTO;
import com.oopsw.clario.dto.history.IncomeDTO;  // 패키지에 맞게 수정

import com.oopsw.clario.repository.history.HistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class HistoryRepostioryTest {
    @Autowired
    HistoryRepository historyRepository;

    @Test
    public void incomeHistoryTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", 1);
        map.put("date", "2025-05");

        List<IncomeHistoryDTO> list = historyRepository.incomeHistory(map);

        for (IncomeHistoryDTO dto : list) {
            System.out.println(dto);
        }
    }
    @Test
    public void expenseHistoryTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", 1);
        map.put("date", "2025-05");
        map.put("categoryName", "식비");
        map.put("cardName", "노리체크");
        List<ExpenseHistoryDTO> list = historyRepository.expenseHistory(map);
        for (ExpenseHistoryDTO dto : list) {
            System.out.println(dto);
        }
    }
    @Test
    public void categoryTest() {
       System.out.println(historyRepository.categoryList());
    }
    @Test
    public void cardTest() {
        System.out.println(historyRepository.cardList(1));
    }
    @Test
    public void accountTest() {
        System.out.println(historyRepository.accountList(1));
    }
    @Test
    public void cardDetailTest() {
        System.out.println(historyRepository.cardDetail(1500));
    }
    @Test
    public void incomeTest() {
    IncomeDTO dto = new IncomeDTO();
    dto.setAccountDay("2025-05-31");
    dto.setSource("업비트(급여)");
    dto.setBankName("국민은행");
    dto.setAccountMoney(5000000L);
    System.out.println(dto);
    }
    @Test
    public void expenseTest() {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setCardDay("2025-05-31");
        dto.setCardStoreName("락볼링장");
        dto.setCardMoney(15000L);
        dto.setCardName("노리체크");
        dto.setCategoryName("여가비");
        System.out.println(dto);
    }
}