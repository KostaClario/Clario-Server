package com.oopsw.clario.repository;

import com.oopsw.clario.dto.MyBankDTO;
import com.oopsw.clario.dto.MyCardDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyDataRepository {
    public List<MyBankDTO> getMyBankConnection(Integer memberId);
    public List<MyCardDTO> getMyCardConnection(Integer memberId);
    public List<MyBankDTO> getMyBankList(Integer memberId);
    public List<MyCardDTO> getMyCardList(Integer memberId);
}
