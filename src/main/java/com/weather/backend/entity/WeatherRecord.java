package com.weather.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private Double temperature; // Celsius

    @Column(nullable = false)
    private Double humidity; // Percentage %

    @Column(nullable = false)
    private Double pressure; // hPa

    @Column(nullable = false)
    private Double windSpeed; // m/s

    @Column(nullable = false, length = 50)
    private String conditionText; // e.g., Sunny, Rainy, Cloudy

    @Column(nullable = false)
    private LocalDateTime recordedAt;
}
