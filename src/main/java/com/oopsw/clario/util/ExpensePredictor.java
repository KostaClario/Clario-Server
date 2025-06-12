package com.oopsw.clario.util;

import java.util.List;

public class ExpensePredictor {

    /**
     * 최근 N개월의 소비 데이터를 기반으로 다음 달 소비 금액을 선형 추정
     * @param expenses 시간 순으로 정렬된 소비 내역 (예: [1월, 2월, 3월])
     * @return 예측된 다음 달 소비 금액
     */
    public static long predictNextMonth(List<Long> expenses) {
        if (expenses == null || expenses.size() < 2) {
            throw new IllegalArgumentException("2개월 이상의 소비 데이터가 필요합니다.");
        }

        int n = expenses.size();
        double sumX = 0, sumY = 0, sumXY = 0, sumXX = 0;

        for (int i = 0; i < n; i++) {
            double x = i + 1;
            double y = expenses.get(i);

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXX += x * x;
        }

        // 기울기 a, 절편 b 계산
        double a = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);
        double b = (sumY - a * sumX) / n;

        double nextX = n + 1;
        double predictedY = a * nextX + b;

        return Math.round(predictedY);
    }
}
