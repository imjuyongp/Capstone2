package com.project.trafficservice.domain.traffic.repository;

import com.project.trafficservice.domain.traffic.entity.BusCongestionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusCongestionRepository extends JpaRepository<BusCongestionData, Long> {
    Optional<BusCongestionData> findByBusNumAndStationNameAndTimeHour(String busNum, String stationName, int timeHour);
}