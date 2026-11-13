package com.weather.backend.controller;

import com.weather.backend.dto.UserSubscriptionDto;
import com.weather.backend.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('METEOROLOGIST') or hasRole('ADMIN')")
public class UserSubscriptionController {

    private final UserSubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<UserSubscriptionDto>> getUserSubscriptions(Principal principal) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(principal.getName()));
    }

    @PostMapping("/location/{locationId}")
    public ResponseEntity<UserSubscriptionDto> subscribe(Principal principal, @PathVariable Long locationId) {
        return new ResponseEntity<>(subscriptionService.subscribe(principal.getName(), locationId), HttpStatus.CREATED);
    }

    @DeleteMapping("/location/{locationId}")
    public ResponseEntity<Void> unsubscribe(Principal principal, @PathVariable Long locationId) {
        subscriptionService.unsubscribe(principal.getName(), locationId);
        return ResponseEntity.noContent().build();
    }
}
