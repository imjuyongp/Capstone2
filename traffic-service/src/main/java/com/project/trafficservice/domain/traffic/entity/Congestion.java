package com.project.trafficservice.domain.traffic.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Congestion {
    EMPTY("거의 없음"),
    LOW("여유"),
    MIDDLE("보통"),
    HIGH("혼집"),
    FULL("매우 혼잡");

    private final String description;
}
