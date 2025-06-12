package com.oopsw.clario.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyDataServiceTests {

    @Autowired
    private MyDataService myDataService;

    @Test
    public void testGetMyBankConnection() {
        System.out.println(myDataService.getMyBankConnection(1));
    }

    @Test
    public void testGetMyCardConnection() {
        System.out.println(myDataService.getMyCardConnection(1));
    }

    @Test
    public void testGetMyBankList() {
        System.out.println(myDataService.getMyBankList(1));
    }

    @Test
    public void testGetMyCardList() {
        System.out.println(myDataService.getMyCardList(1));
    }
}
