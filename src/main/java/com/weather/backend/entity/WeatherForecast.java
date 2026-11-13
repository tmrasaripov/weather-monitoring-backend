package com.weather.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "weather_forecasts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private LocalDate forecastDate;

    @Column(nullable = false)
    private Double tempMin;

    @Column(nullable = false)
    private Double tempMax;

    @Column(nullable = false)
    private Double precipitationProbability; // %

    @Column(nullable = false, length = 100)
    private String summary;
}
