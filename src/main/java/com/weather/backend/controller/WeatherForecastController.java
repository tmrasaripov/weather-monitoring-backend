package com.weather.backend.controller;

import com.weather.backend.dto.WeatherForecastDto;
import com.weather.backend.service.WeatherForecastService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forecasts")
@RequiredArgsConstructor
public class WeatherForecastController {

    private final WeatherForecastService weatherForecastService;

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<WeatherForecastDto>> getForecastsByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(weatherForecastService.getForecastsByLocation(locationId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('METEOROLOGIST', 'ADMIN')")
    public ResponseEntity<WeatherForecastDto> createForecast(@Valid @RequestBody WeatherForecastDto dto) {
        return new ResponseEntity<>(weatherForecastService.createForecast(dto), HttpStatus.CREATED);
    }
}
