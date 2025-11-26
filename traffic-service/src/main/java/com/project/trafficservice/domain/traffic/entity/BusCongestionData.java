package com.project.trafficservice.domain.traffic.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bus_congestion")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusCongestionData { // 내부 데이터 모델(csv 데이터 용)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String busNum; // 노선번호

    @Column(nullable = false)
    private String stationName; // 역명

    @Column(nullable = false)
    private int timeHour; // 시간 (0-23)

    @Column(nullable = false)
    private int boarding; // 승차인원

    @Column(nullable = false)
    private int alighting; // 하차인원

    @Column(nullable = false)
    private int congestionLevel; // 혼잡도 (0-4)
}
