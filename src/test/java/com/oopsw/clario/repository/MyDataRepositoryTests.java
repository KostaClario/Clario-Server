package com.oopsw.clario.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyDataRepositoryTests {
    @Autowired
    private MyDataRepository myDataRepository;

    @Test
    public void testGetMyBankConnection(){
        System.out.println(myDataRepository.getMyBankConnection(1));
    }

    @Test
    public void testGetMyCardConnection(){
        System.out.println(myDataRepository.getMyCardConnection(1));
    }

    @Test
    public void testGetMyBankList(){
        System.out.println(myDataRepository.getMyBankList(1));
    }

    @Test
    public void testGetMyCardList(){
        System.out.println(myDataRepository.getMyCardList(1));
    }



}
