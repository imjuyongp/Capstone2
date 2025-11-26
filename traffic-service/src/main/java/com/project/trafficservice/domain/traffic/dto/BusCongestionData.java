package com.project.trafficservice.domain.traffic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusCongestionData { // 내부 데이터 모델(csv 데이터 용)
    private String busNum; // 노선번호
    private String stationName; // 역명
    private int hour; // 시간 (0-23)
    private int boarding; // 승차인원
    private int alighting; // 하차인원
    private int congestionLevel; // 혼잡도 (0-4)
}
