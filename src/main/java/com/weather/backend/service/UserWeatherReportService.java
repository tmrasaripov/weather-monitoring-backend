package com.weather.backend.service;

import com.weather.backend.dto.UserWeatherReportDto;
import com.weather.backend.entity.Location;
import com.weather.backend.entity.User;
import com.weather.backend.entity.UserWeatherReport;
import com.weather.backend.exception.ResourceNotFoundException;
import com.weather.backend.repository.LocationRepository;
import com.weather.backend.repository.UserRepository;
import com.weather.backend.repository.UserWeatherReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserWeatherReportService {

    private final UserWeatherReportRepository reportRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public UserWeatherReportDto createReport(String username, UserWeatherReportDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + dto.getLocationId()));

        UserWeatherReport report = UserWeatherReport.builder()
                .user(user)
                .location(location)
                .temperature(dto.getTemperature())
                .conditionText(dto.getConditionText())
                .comment(dto.getComment())
                .status(UserWeatherReport.ModerationStatus.PENDING)
                .reportedAt(LocalDateTime.now())
                .build();

        UserWeatherReport saved = reportRepository.save(report);
        return mapToDto(saved);
    }

    public List<UserWeatherReportDto> getReportsByStatus(UserWeatherReport.ModerationStatus status) {
        return reportRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserWeatherReportDto> getUserReports(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return reportRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserWeatherReportDto moderateReport(Long reportId, UserWeatherReport.ModerationStatus status) {
        UserWeatherReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

        report.setStatus(status);
        UserWeatherReport updated = reportRepository.save(report);
        return mapToDto(updated);
    }

    public UserWeatherReportDto mapToDto(UserWeatherReport report) {
        return new UserWeatherReportDto(
                report.getId(),
                report.getUser().getId(),
                report.getUser().getUsername(),
                report.getLocation().getId(),
                report.getLocation().getName(),
                report.getTemperature(),
                report.getConditionText(),
                report.getComment(),
                report.getStatus(),
                report.getReportedAt()
        );
    }
}
