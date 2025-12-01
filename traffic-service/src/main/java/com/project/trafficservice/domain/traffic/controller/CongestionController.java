package com.project.trafficservice.domain.traffic.controller;

import com.project.trafficservice.domain.traffic.dto.request.BusCongestionRequest;
import com.project.trafficservice.domain.traffic.dto.response.BusCongestionResponse;
import com.project.trafficservice.domain.traffic.service.CongestionService;
import com.project.trafficservice.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/congestion")
@RequiredArgsConstructor
@Tag(name = "Congestion", description = "혼잡도 관련 API")
public class CongestionController {

    private final CongestionService congestionService;

    @Operation(summary = "버스 혼잡도 예측", description = "버스 번호, 정류장명, 출발 시간을 기반으로 혼잡도를 예측합니다.")
    @PostMapping("")
    public ResponseEntity<BaseResponse<BusCongestionResponse>> predictCongestion(
        @Valid @RequestBody BusCongestionRequest request) {

        BusCongestionResponse response = congestionService.predictCongestion(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }

    @Operation(summary = "버스 혼잡도 조회", description = "Query Parameter로 혼잡도를 조회합니다.")
    @GetMapping
    public ResponseEntity<BaseResponse<BusCongestionResponse>> getCongestion(
        @RequestParam String busNum,
        @RequestParam String stationName,
        @RequestParam Integer departureTime) {

        BusCongestionRequest request = BusCongestionRequest.builder()
            .busNum(busNum)
            .stationName(stationName)
            .departureTime(departureTime)
            .build();

        BusCongestionResponse response = congestionService.predictCongestion(request);
        return ResponseEntity.ok(BaseResponse.success(response));
    }


}
