package com.oopsw.clario.repository.history;

import com.oopsw.clario.dto.history.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HistoryRepository {
    List<AccountDTO> accountList(Integer memberId);

    List<CardDTO> cardList(Integer memberId);

    CardDetailDTO cardDetail(int cardTradeId);

    List<ExpenseHistoryDTO> expenseHistory(Map<String, Object> params);

    List<IncomeHistoryDTO> incomeHistory(Map<String, Object> income);

    int income(IncomeDTO dto);

    int expense(ExpenseDTO dto);

    List<Map<String, Object>> categoryList();


}
