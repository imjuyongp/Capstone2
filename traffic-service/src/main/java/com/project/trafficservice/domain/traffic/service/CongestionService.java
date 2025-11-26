package com.project.trafficservice.domain.traffic.service;

import com.project.trafficservice.domain.traffic.dto.BusCongestionData;
import com.project.trafficservice.domain.traffic.dto.request.BusCongestionRequest;
import com.project.trafficservice.domain.traffic.dto.response.BusCongestionResponse;
import com.project.trafficservice.domain.traffic.entity.Congestion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CongestionService {

    private final CsvDataLoad csvDataLoad;

    public BusCongestionResponse predictCongestion(BusCongestionRequest request) {
        log.info("혼잡도 예측 요청: busNum={}, station={}, time={}",
            request.getBusNum(), request.getStationName(), request.getDepartureTime());

        // 캐시에서 데이터 조회
        Optional<BusCongestionData> dataOpt = csvDataLoad.getCongestion(
            request.getBusNum(),
            request.getStationName(),
            request.getDepartureTime()
        );

        if (dataOpt.isEmpty()) {
            log.error("해당 조건의 데이터를 찾을 수 없습니다.");
        }

        BusCongestionData data = dataOpt.get();
        Congestion congestion = Congestion.fromLevel(data.getCongestionLevel());

        return BusCongestionResponse.builder()
            .busNum(data.getBusNum())
            .congestion(congestion)
            .description(congestion.generateDescription(congestion, data))
            .build();

    }

}
