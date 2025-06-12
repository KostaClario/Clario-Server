package com.oopsw.clario.repository;

import com.oopsw.clario.dto.dashboard.MemberDateDTO;
import com.oopsw.clario.dto.dashboard.TradeDTO;
import com.oopsw.clario.dto.dashboard.Top3CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface DashboardRepository {
    public Long getMonthlyIncome(MemberDateDTO memberDateDTO);
    public Long getMonthlyExpense(MemberDateDTO memberDateDTO);
    public Long getTargetAssets(Integer memberId);
    public Long getTotalAssets(Integer memberId);
    public boolean addTargetAssets(HashMap<String, Object> targetAssets);
    public List<TradeDTO> getTodayExpense(MemberDateDTO memberDateDTO);
    public List<TradeDTO> getTodayIncome(MemberDateDTO memberDateDTO);
    public List<Top3CategoryDTO> getTop3Category(MemberDateDTO memberDateDTO);
    public List<TradeDTO> getYearsExpenses(MemberDateDTO memberDateDTO);
    public List<TradeDTO> getYearsIncomes(MemberDateDTO memberDateDTO);

}
