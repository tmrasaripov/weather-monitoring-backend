package com.weather.backend.service;

import com.weather.backend.dto.WeatherAlertDto;
import com.weather.backend.entity.Location;
import com.weather.backend.entity.WeatherAlert;
import com.weather.backend.exception.ResourceNotFoundException;
import com.weather.backend.repository.LocationRepository;
import com.weather.backend.repository.WeatherAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherAlertService {

    private final WeatherAlertRepository weatherAlertRepository;
    private final LocationRepository locationRepository;

    public List<WeatherAlertDto> getAllActiveAlerts() {
        return weatherAlertRepository.findAllActiveAlerts(LocalDateTime.now()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<WeatherAlertDto> getActiveAlertsByLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found with id: " + locationId);
        }
        return weatherAlertRepository.findActiveAlertsByLocation(locationId, LocalDateTime.now()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WeatherAlertDto createAlert(WeatherAlertDto dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + dto.getLocationId()));

        WeatherAlert alert = WeatherAlert.builder()
                .location(location)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .severity(dto.getSeverity())
                .issuedAt(dto.getIssuedAt() != null ? dto.getIssuedAt() : LocalDateTime.now())
                .expiresAt(dto.getExpiresAt())
                .build();

        WeatherAlert saved = weatherAlertRepository.save(alert);
        return mapToDto(saved);
    }

    public WeatherAlertDto mapToDto(WeatherAlert alert) {
        return new WeatherAlertDto(
                alert.getId(),
                alert.getLocation().getId(),
                alert.getLocation().getName(),
                alert.getTitle(),
                alert.getDescription(),
                alert.getSeverity(),
                alert.getIssuedAt(),
                alert.getExpiresAt()
        );
    }
}
