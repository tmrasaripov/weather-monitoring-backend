package com.weather.backend.service;

import com.weather.backend.dto.WeatherForecastDto;
import com.weather.backend.entity.Location;
import com.weather.backend.entity.WeatherForecast;
import com.weather.backend.exception.ResourceNotFoundException;
import com.weather.backend.repository.LocationRepository;
import com.weather.backend.repository.WeatherForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherForecastService {

    private final WeatherForecastRepository weatherForecastRepository;
    private final LocationRepository locationRepository;

    public List<WeatherForecastDto> getForecastsByLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException("Location not found with id: " + locationId);
        }
        return weatherForecastRepository.findByLocationIdAndForecastDateGreaterThanEqualOrderByForecastDateAsc(locationId, LocalDate.now())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public WeatherForecastDto createForecast(WeatherForecastDto dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + dto.getLocationId()));

        WeatherForecast forecast = WeatherForecast.builder()
                .location(location)
                .forecastDate(dto.getForecastDate())
                .tempMin(dto.getTempMin())
                .tempMax(dto.getTempMax())
                .precipitationProbability(dto.getPrecipitationProbability())
                .summary(dto.getSummary())
                .build();

        WeatherForecast saved = weatherForecastRepository.save(forecast);
        return mapToDto(saved);
    }

    public WeatherForecastDto mapToDto(WeatherForecast forecast) {
        return new WeatherForecastDto(
                forecast.getId(),
                forecast.getLocation().getId(),
                forecast.getLocation().getName(),
                forecast.getForecastDate(),
                forecast.getTempMin(),
                forecast.getTempMax(),
                forecast.getPrecipitationProbability(),
                forecast.getSummary()
        );
    }
}
