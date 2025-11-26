package com.project.trafficservice.domain.traffic.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.project.trafficservice.domain.traffic.entity.BusCongestionData;
import com.project.trafficservice.domain.traffic.repository.BusCongestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvDataLoad implements ApplicationRunner {

    private static final String CSV_PATH = "/Users/parkjuyong/Desktop/4-1/Capstone/capstone2/traffic-service/src/main/java/com/project/trafficservice/domain/traffic/assets/09월_승하차인원별_혼잡도.csv";
    private final BusCongestionRepository repository;

    @Override
    @Async // 비동기로 별도의 메소드 풀에서 실행되도록 함
    @Transactional
    public void run(ApplicationArguments args) {
        loadCsvData();
    }

    // 어플리케이션 시작 후 비동기로 csv 파일 로드하여 DB에 저장
    private void loadCsvData() {
        // 이미 데이터가 있으면 스킵
        if (repository.count() > 0) {
            log.info("DB에 이미 데이터가 존재합니다. CSV 로딩을 스킵합니다.");
            return;
        }

        log.info("백그라운드에서 CSV 데이터 로딩 시작");
        long startTime = System.currentTimeMillis();

        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            List<String[]> rows = reader.readAll();
            List<BusCongestionData> dataList = new ArrayList<>();

            for(int i=1; i<rows.size(); i++) { // 0행은 헤더이므로 스킵
                String[] row = rows.get(i);

                // CSV 파싱
                String busNum = row[0]; // 버스 번호
                String stationName = row[1]; // 역명
                int timeHour = Integer.parseInt(row[2]); // 시간
                int boarding = Integer.parseInt(row[3]); // 승차인원
                int alighting = Integer.parseInt(row[4]); // 하차인원
                int congestionLevel = Integer.parseInt(row[5]); // 혼잡도 레벨(0-4)

                BusCongestionData data = BusCongestionData.builder()
                    .busNum(busNum)
                    .stationName(stationName)
                    .timeHour(timeHour)
                    .boarding(boarding)
                    .alighting(alighting)
                    .congestionLevel(congestionLevel)
                    .build();

                dataList.add(data);

                // 배치 저장 (1000개씩)
                if (dataList.size() >= 1000) {
                    repository.saveAll(dataList);
                    dataList.clear();

                    // 10000개마다 로그 출력
                    if (i % 10000 == 0) {
                        log.info("CSV 로딩 진행 중: {}개 처리됨", i);
                    }
                }
            }

            // 나머지 데이터 저장
            if (!dataList.isEmpty()) {
                repository.saveAll(dataList);
            }

            long endTime = System.currentTimeMillis();
            log.info("CSV 데이터 로딩 완료! 총 {}개, 소요 시간: {}ms",
                repository.count(), (endTime - startTime));

        } catch (IOException | CsvException e) {
            log.error("CSV 파일 로딩 실패", e);
            throw new RuntimeException("CSV 데이터 로드 실패", e);
        }
    }
}
