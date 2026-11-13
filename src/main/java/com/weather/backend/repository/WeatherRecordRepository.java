package com.weather.backend.repository;

import com.weather.backend.entity.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {
    List<WeatherRecord> findByLocationIdOrderByRecordedAtDesc(Long locationId);
    Optional<WeatherRecord> findTopByLocationIdOrderByRecordedAtDesc(Long locationId);
}
