package com.weather.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionDto {
    private Long id;
    private Long userId;
    private String username;

    @NotNull(message = "Location ID is required")
    private Long locationId;
    private String locationName;
    private String locationCountry;

    private LocalDateTime subscribedAt;
}
