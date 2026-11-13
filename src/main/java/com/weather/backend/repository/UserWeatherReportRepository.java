package com.weather.backend.repository;

import com.weather.backend.entity.UserWeatherReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWeatherReportRepository extends JpaRepository<UserWeatherReport, Long> {
    List<UserWeatherReport> findByUserId(Long userId);
    List<UserWeatherReport> findByLocationIdAndStatus(Long locationId, UserWeatherReport.ModerationStatus status);
    List<UserWeatherReport> findByStatus(UserWeatherReport.ModerationStatus status);
}
