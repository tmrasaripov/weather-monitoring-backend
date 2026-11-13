package com.weather.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherRecordDto {
    private Long id;

    @NotNull(message = "Location ID is required")
    private Long locationId;
    private String locationName;

    @NotNull(message = "Temperature is required")
    private Double temperature;

    @NotNull(message = "Humidity is required")
    private Double humidity;

    @NotNull(message = "Pressure is required")
    private Double pressure;

    @NotNull(message = "Wind speed is required")
    private Double windSpeed;

    @NotBlank(message = "Condition text is required")
    private String conditionText;

    private LocalDateTime recordedAt;
}
