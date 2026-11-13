package com.weather.backend.service;

import com.weather.backend.dto.LocationDto;
import com.weather.backend.entity.Location;
import com.weather.backend.exception.BadRequestException;
import com.weather.backend.exception.ResourceNotFoundException;
import com.weather.backend.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationDto> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public LocationDto getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        return mapToDto(location);
    }

    @Transactional
    public LocationDto createLocation(LocationDto dto) {
        if (locationRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new BadRequestException("Location with name '" + dto.getName() + "' already exists");
        }

        Location location = Location.builder()
                .name(dto.getName())
                .country(dto.getCountry())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();

        Location saved = locationRepository.save(location);
        return mapToDto(saved);
    }

    @Transactional
    public LocationDto updateLocation(Long id, LocationDto dto) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));

        location.setName(dto.getName());
        location.setCountry(dto.getCountry());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());

        Location updated = locationRepository.save(location);
        return mapToDto(updated);
    }

    @Transactional
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location not found with id: " + id);
        }
        locationRepository.deleteById(id);
    }

    public LocationDto mapToDto(Location location) {
        return new LocationDto(
                location.getId(),
                location.getName(),
                location.getCountry(),
                location.getLatitude(),
                location.getLongitude()
        );
    }
}
