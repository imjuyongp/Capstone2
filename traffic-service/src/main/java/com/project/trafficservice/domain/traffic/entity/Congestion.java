package com.project.trafficservice.domain.traffic.entity;

import com.project.trafficservice.domain.traffic.dto.BusCongestionData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public enum Congestion {
    EMPTY(0,"거의 없음"),
    LOW(1,"여유"),
    MIDDLE(2,"보통"),
    HIGH(3,"혼집"),
    FULL(4,"매우 혼잡");

    private final int level;
    private final String description;

    public static Congestion fromLevel(int level) { // 혼잡도 레벨에 따른 혼잡도 래이블 반환
        for (Congestion congestion : values()) {
            if (congestion.level == level) {
                return congestion;
            }
        }
        throw new IllegalArgumentException("Invalid congestion level: " + level);
    }

    public String generateDescription(Congestion congestion, BusCongestionData data) {
        switch (congestion) {
            case EMPTY:
                return "승객이 거의 없습니다. 편안하게 이용하실 수 있습니다.";
            case LOW:
                return String.format("여유롭습니다. 평균 %d명이 탑승합니다.", data.getBoarding());
            case MIDDLE:
                return String.format("보통 수준입니다. 평균 %d명이 탑승합니다.", data.getBoarding());
            case HIGH:
                return String.format("혼잡합니다. 평균 %d명이 탑승하며, 자리가 부족할 수 있습니다.",
                    data.getBoarding());
            case FULL:
                return String.format("매우 혼잡합니다. 평균 %d명이 탑승하며, 다음 버스 이용을 권장합니다.",
                    data.getBoarding());
            default:
                return "혼잡도 정보를 확인할 수 없습니다.";
        }
    }
}
