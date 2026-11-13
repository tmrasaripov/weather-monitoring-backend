package com.weather.backend.service;

import com.weather.backend.dto.WeatherRecordDto;
import com.weather.backend.entity.Location;
import com.weather.backend.entity.WeatherRecord;
import com.weather.backend.exception.ResourceNotFoundException;
import com.weather.backend.repository.LocationRepository;
import com.weather.backend.repository.WeatherRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherRecordService {

    private final WeatherRecordRepository weatherRecordRepository;
    private final LocationRepository locationRepository;

    public List<WeatherRecordDto> getRecordsByLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found with id: " + locationId);
        }
        return weatherRecordRepository.findByLocationIdOrderByRecordedAtDesc(locationId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public WeatherRecordDto getLatestRecordByLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found with id: " + locationId);
        }
        WeatherRecord record = weatherRecordRepository.findTopByLocationIdOrderByRecordedAtDesc(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("No weather records found for location id: " + locationId));
        return mapToDto(record);
    }

    @Transactional
    public WeatherRecordDto createRecord(WeatherRecordDto dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + dto.getLocationId()));

        WeatherRecord record = WeatherRecord.builder()
                .location(location)
                .temperature(dto.getTemperature())
                .humidity(dto.getHumidity())
                .pressure(dto.getPressure())
                .windSpeed(dto.getWindSpeed())
                .conditionText(dto.getConditionText())
                .recordedAt(dto.getRecordedAt() != null ? dto.getRecordedAt() : LocalDateTime.now())
                .build();

        WeatherRecord saved = weatherRecordRepository.save(record);
        return mapToDto(saved);
    }

    public WeatherRecordDto mapToDto(WeatherRecord record) {
        return new WeatherRecordDto(
                record.getId(),
                record.getLocation().getId(),
                record.getLocation().getName(),
                record.getTemperature(),
                record.getHumidity(),
                record.getPressure(),
                record.getWindSpeed(),
                record.getConditionText(),
                record.getRecordedAt()
        );
    }
}
