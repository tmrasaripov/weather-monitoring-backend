package com.weather.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastDto {
    private Long id;

    @NotNull(message = "Location ID is required")
    private Long locationId;
    private String locationName;

    @NotNull(message = "Forecast date is required")
    private LocalDate forecastDate;

    @NotNull(message = "Minimum temperature is required")
    private Double tempMin;

    @NotNull(message = "Maximum temperature is required")
    private Double tempMax;

    @NotNull(message = "Precipitation probability is required")
    private Double precipitationProbability;

    @NotBlank(message = "Summary is required")
    private String summary;
}
