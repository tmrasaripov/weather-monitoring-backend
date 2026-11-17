package com.weather.backend.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.weather.backend.entity.Location;
import com.weather.backend.entity.WeatherRecord;
import com.weather.backend.repository.LocationRepository;
import com.weather.backend.repository.WeatherRecordRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherSyncService {

    private final LocationRepository locationRepository;
    private final WeatherRecordRepository weatherRecordRepository;
    private final RestClient restClient = RestClient.create();

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Scheduled(initialDelay = 5000, fixedRate = 1800000)
    public void syncRealTimeWeather() {
        List<Location> locations = locationRepository.findAll();
        if (locations.isEmpty()) {
            log.warn("No locations found in database to sync weather.");
            return;
        }

        log.info("Starting automatic weather sync via OpenWeatherMap for {} locations...", locations.size());

        for (Location location : locations) {
            try {
                OpenWeatherResponse response = restClient.get()
                        .uri(apiUrl + "?lat={lat}&lon={lon}&appid={key}&units=metric",
                                location.getLatitude(),
                                location.getLongitude(),
                                apiKey)
                        .retrieve()
                        .body(OpenWeatherResponse.class);

                if (response != null && response.getMain() != null) {
                    String condition = (response.getWeather() != null && !response.getWeather().isEmpty())
                            ? response.getWeather().get(0).getMain()
                            : "Unknown";

                    double windSpeed = (response.getWind() != null && response.getWind().getSpeed() != null)
                            ? response.getWind().getSpeed()
                            : 0.0;

                    WeatherRecord record = WeatherRecord.builder()
                            .location(location)
                            .temperature(response.getMain().getTemp())
                            .humidity(response.getMain().getHumidity())
                            .pressure(response.getMain().getPressure())
                            .windSpeed(windSpeed)
                            .conditionText(condition)
                            .recordedAt(LocalDateTime.now())
                            .build();

                    weatherRecordRepository.save(record);
                    log.info("Synced real weather for {}: {}°C, {}, humidity {}%",
                            location.getName(), record.getTemperature(), record.getConditionText(), record.getHumidity());
                }
            } catch (Exception e) {
                log.error("Failed to fetch weather from OpenWeatherMap for {}: {}", location.getName(), e.getMessage());
            }
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenWeatherResponse {
        private MainData main;
        private WindData wind;
        private List<WeatherDescription> weather;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class MainData {
        private Double temp;
        private Double humidity;
        private Double pressure;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WindData {
        private Double speed;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WeatherDescription {
        private String main;
    }
}