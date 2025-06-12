package com.oopsw.clario.service;

import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import com.oopsw.clario.repository.MyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyDataService {
    @Autowired
    private MyDataRepository myDataRepository;

    public List<MyBankDTO> getMyBankConnection(Integer memberId) {
        return myDataRepository.getMyBankConnection(memberId);
    }

    public List<MyCardDTO> getMyCardConnection(Integer memberId) {
        return myDataRepository.getMyCardConnection(memberId);
    }

    public List<MyBankDTO> getMyBankList(Integer memberId) {
        return myDataRepository.getMyBankList(memberId);
    }

    public List<MyCardDTO> getMyCardList(Integer memberId) {
        return myDataRepository.getMyCardList(memberId);
    }
}
