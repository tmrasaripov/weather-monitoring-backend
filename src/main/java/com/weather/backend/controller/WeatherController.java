package com.weather.backend.controller;

import com.weather.backend.dto.WeatherRecordDto;
import com.weather.backend.service.WeatherRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherRecordService weatherRecordService;

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<WeatherRecordDto>> getRecordsByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(weatherRecordService.getRecordsByLocation(locationId));
    }

    @GetMapping("/location/{locationId}/latest")
    public ResponseEntity<WeatherRecordDto> getLatestRecordByLocation(@PathVariable Long locationId) {
        return ResponseEntity.ok(weatherRecordService.getLatestRecordByLocation(locationId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('METEOROLOGIST', 'ADMIN')")
    public ResponseEntity<WeatherRecordDto> createRecord(@Valid @RequestBody WeatherRecordDto dto) {
        return new ResponseEntity<>(weatherRecordService.createRecord(dto), HttpStatus.CREATED);
    }
}
