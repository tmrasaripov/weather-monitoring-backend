package com.weather.backend.service;

import com.weather.backend.dto.UserSubscriptionDto;
import com.weather.backend.entity.Location;
import com.weather.backend.entity.User;
import com.weather.backend.entity.UserSubscription;
import com.weather.backend.exception.BadRequestException;
import com.weather.backend.exception.ResourceNotFoundException;
import com.weather.backend.repository.LocationRepository;
import com.weather.backend.repository.UserSubscriptionRepository;
import com.weather.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private final UserSubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public List<UserSubscriptionDto> getUserSubscriptions(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return subscriptionRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserSubscriptionDto subscribe(String username, Long locationId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + locationId));

        if (subscriptionRepository.existsByUserIdAndLocationId(user.getId(), locationId)) {
            throw new BadRequestException("User is already subscribed to this location");
        }

        UserSubscription subscription = UserSubscription.builder()
                .user(user)
                .location(location)
                .subscribedAt(LocalDateTime.now())
                .build();

        UserSubscription saved = subscriptionRepository.save(subscription);
        return mapToDto(saved);
    }

    @Transactional
    public void unsubscribe(String username, Long locationId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!subscriptionRepository.existsByUserIdAndLocationId(user.getId(), locationId)) {
            throw new ResourceNotFoundException("Subscription not found for this location");
        }

        subscriptionRepository.deleteByUserIdAndLocationId(user.getId(), locationId);
    }

    public UserSubscriptionDto mapToDto(UserSubscription sub) {
        return new UserSubscriptionDto(
                sub.getId(),
                sub.getUser().getId(),
                sub.getUser().getUsername(),
                sub.getLocation().getId(),
                sub.getLocation().getName(),
                sub.getLocation().getCountry(),
                sub.getSubscribedAt()
        );
    }
}
