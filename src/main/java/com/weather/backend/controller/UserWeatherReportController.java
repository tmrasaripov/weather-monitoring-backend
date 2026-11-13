package com.weather.backend.controller;

import com.weather.backend.dto.UserWeatherReportDto;
import com.weather.backend.entity.UserWeatherReport;
import com.weather.backend.service.UserWeatherReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class UserWeatherReportController {

    private final UserWeatherReportService reportService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('METEOROLOGIST') or hasRole('ADMIN')")
    public ResponseEntity<UserWeatherReportDto> createReport(Principal principal, @Valid @RequestBody UserWeatherReportDto dto) {
        return new ResponseEntity<>(reportService.createReport(principal.getName(), dto), HttpStatus.CREATED);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER') or hasRole('METEOROLOGIST') or hasRole('ADMIN')")
    public ResponseEntity<List<UserWeatherReportDto>> getMyReports(Principal principal) {
        return ResponseEntity.ok(reportService.getUserReports(principal.getName()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('METEOROLOGIST', 'ADMIN')")
    public ResponseEntity<List<UserWeatherReportDto>> getPendingReports() {
        return ResponseEntity.ok(reportService.getReportsByStatus(UserWeatherReport.ModerationStatus.PENDING));
    }

    @PatchMapping("/{reportId}/moderate")
    @PreAuthorize("hasAnyRole('METEOROLOGIST', 'ADMIN')")
    public ResponseEntity<UserWeatherReportDto> moderateReport(
            @PathVariable Long reportId,
            @RequestParam UserWeatherReport.ModerationStatus status) {
        return ResponseEntity.ok(reportService.moderateReport(reportId, status));
    }
}
