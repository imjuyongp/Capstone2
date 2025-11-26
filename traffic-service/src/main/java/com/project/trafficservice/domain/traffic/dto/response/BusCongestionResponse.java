package com.project.trafficservice.domain.traffic.dto.response;

import com.project.trafficservice.domain.traffic.entity.Congestion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "BusCongetionRequest DTO", description = "사용자 탑승 버스 혼잡도 요청")
public class BusCongestionResponse {
    @Schema(description = "버스 번호")
    private String busNum;

    @Schema(description = "혼잡도")
    private Congestion congestion;
}
