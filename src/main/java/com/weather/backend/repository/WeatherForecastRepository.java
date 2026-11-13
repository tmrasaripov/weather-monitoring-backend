package com.weather.backend.repository;

import com.weather.backend.entity.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {
    List<WeatherForecast> findByLocationIdAndForecastDateGreaterThanEqualOrderByForecastDateAsc(Long locationId, LocalDate date);
    List<WeatherForecast> findByLocationIdOrderByForecastDateAsc(Long locationId);
}
