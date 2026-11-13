package com.weather.backend.controller;

import com.weather.backend.dto.WeatherAlertDto;
import com.weather.backend.service.WeatherAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class WeatherAlertController {

    private final WeatherAlertService weatherAlertService;

    @GetMapping
    public ResponseEntity<List<WeatherAlertDto>> getAllActiveAlerts() {
        return ResponseEntity.ok(weatherAlertService.getAllActiveAlerts());
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<WeatherAlertDto>> getActiveAlertsByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(weatherAlertService.getActiveAlertsByLocation(locationId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('METEOROLOGIST', 'ADMIN')")
    public ResponseEntity<WeatherAlertDto> createAlert(@Valid @RequestBody WeatherAlertDto dto) {
        return new ResponseEntity<>(weatherAlertService.createAlert(dto), HttpStatus.CREATED);
    }
}
