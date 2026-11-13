package com.weather.backend.repository;

import com.weather.backend.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    List<UserSubscription> findByUserId(Long userId);
    Optional<UserSubscription> findByUserIdAndLocationId(Long userId, Long locationId);
    boolean existsByUserIdAndLocationId(Long userId, Long locationId);
    void deleteByUserIdAndLocationId(Long userId, Long locationId);
}
