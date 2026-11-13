package com.weather.backend.dto;

import com.weather.backend.entity.UserWeatherReport;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWeatherReportDto {
    private Long id;
    private Long userId;
    private String username;

    @NotNull(message = "Location ID is required")
    private Long locationId;
    private String locationName;

    @NotNull(message = "Temperature is required")
    private Double temperature;

    @NotBlank(message = "Condition text is required")
    private String conditionText;

    private String comment;
    private UserWeatherReport.ModerationStatus status;
    private LocalDateTime reportedAt;
}
