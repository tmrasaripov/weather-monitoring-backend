package com.weather.backend.dto;

import com.weather.backend.entity.WeatherAlert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherAlertDto {
    private Long id;

    @NotNull(message = "Location ID is required")
    private Long locationId;
    private String locationName;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Severity is required")
    private WeatherAlert.Severity severity;

    private LocalDateTime issuedAt;

    @NotNull(message = "Expires at timestamp is required")
    private LocalDateTime expiresAt;
}
