package com.weather.backend.repository;

import com.weather.backend.entity.WeatherAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherAlertRepository extends JpaRepository<WeatherAlert, Long> {
    List<WeatherAlert> findByLocationId(Long locationId);

    @Query("SELECT a FROM WeatherAlert a WHERE a.expiresAt > :now ORDER BY a.issuedAt DESC")
    List<WeatherAlert> findAllActiveAlerts(@Param("now") LocalDateTime now);

    @Query("SELECT a FROM WeatherAlert a WHERE a.location.id = :locationId AND a.expiresAt > :now ORDER BY a.issuedAt DESC")
    List<WeatherAlert> findActiveAlertsByLocation(@Param("locationId") Long locationId, @Param("now") LocalDateTime now);
}
