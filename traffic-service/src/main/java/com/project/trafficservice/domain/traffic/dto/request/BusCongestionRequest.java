package com.project.trafficservice.domain.traffic.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "BusCongetionRequest DTO", description = "사용자 탑승 버스의 출발 정류장 기준 혼잡도 요청")
public class BusCongestionRequest {
    @Schema(description = "버스 번호")
    @NotBlank(message = "버스 번호는 필수 입니다.")
    private String busNum;

    @Schema(description = "장류장 명")
    @NotBlank(message = "정류장명은 필수입니다")
    private String stationName;

    @Schema(description = "출발 시간")
    @NotBlank(message = "출발 시간은 필수입니다")
    private Integer departureTime;

}
