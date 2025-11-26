package com.project.trafficservice.domain.traffic.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.project.trafficservice.domain.traffic.dto.BusCongestionData;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvDataLoad {

    private static final String CSV_PATH = "/Users/parkjuyong/Desktop/4-1/Capstone/capstone2/traffic-service/src/main/java/com/project/trafficservice/domain/traffic/assets/09월_승하차인원별_혼잡도.csv";
    private Map<String, BusCongestionData> dataCache = new HashMap<>();

    // 어플리케이션 시작 시 csv 파일 로드
    @PostConstruct // 스프링 빈이 생성되고 의존성 주입이 모두 끝난 후 딱 한번만 실행되도록 해주는 어노테이션
    public void loadCsvData() {
        log.info("csv 데이터 로딩 시작");
        long startTime = System.currentTimeMillis();

        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            List<String[]> rows = reader.readAll(); // CSV 파일의 모든 행을 한 번에 읽어서 List<String[]> 로 반환

            for(int i=1; i<rows.size(); i++) { // 0행은 헤더이므로 스킵
                String[] row = rows.get(i);

                // CSV 파싱
                String busNum = row[0]; // 버스 번호
                String stationName = row[1]; // 역명
                int hour = Integer.parseInt(row[2]); // 시간
                int boarding = Integer.parseInt(row[3]); // 승차인원
                int alighting = Integer.parseInt(row[4]); // 히차인원
                int congestionLevel = Integer.parseInt(row[5]); // 혼잡도 레벨(0-4)

                BusCongestionData data = BusCongestionData.builder()
                    .busNum(busNum)
                    .stationName(stationName)
                    .hour(hour)
                    .boarding(boarding)
                    .alighting(alighting)
                    .congestionLevel(congestionLevel)
                    .build();

                String key = createKey(busNum, stationName, hour);
                dataCache.put(key, data);
            }

            long endTime = System.currentTimeMillis();
            log.info("CSV 데이터 로딩 완료! 총 {}개, 소요 시간: {}ms",
                dataCache.size(), (endTime - startTime));

        } catch (IOException | CsvException e) {
            log.error("CSV 파일 로딩 실패", e);
            throw new RuntimeException("CSV 데이터 로드 실패", e);
        }

    }

    private String createKey(String busNum, String stationName, int hour) {
        return busNum + "_" + stationName + "_" + hour;
    }

    public Optional<BusCongestionData> getCongestion(String busNum, String stationName, int hour) {
        String key = createKey(busNum, stationName, hour);
        return Optional.ofNullable(dataCache.get(key));
    }

}
