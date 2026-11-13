package com.weather.backend.component;

import com.weather.backend.entity.*;
import com.weather.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final WeatherRecordRepository weatherRecordRepository;
    private final WeatherForecastRepository weatherForecastRepository;
    private final WeatherAlertRepository weatherAlertRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserWeatherReportRepository userWeatherReportRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) return;

        User admin = User.builder().username("admin").email("admin@weather.com")
                .password(passwordEncoder.encode("admin123")).role(User.Role.ROLE_ADMIN).build();
        userRepository.save(admin);

        User meteorologist = User.builder().username("meteo").email("meteo@weather.com")
                .password(passwordEncoder.encode("meteo123")).role(User.Role.ROLE_METEOROLOGIST).build();
        userRepository.save(meteorologist);

        User regularUser = User.builder().username("user").email("user@weather.com")
                .password(passwordEncoder.encode("user123")).role(User.Role.ROLE_USER).build();
        userRepository.save(regularUser);

        Location moscow = Location.builder().name("Moscow").country("Russia").latitude(55.7558).longitude(37.6173).build();
        locationRepository.save(moscow);

        Location spb = Location.builder().name("Saint Petersburg").country("Russia").latitude(59.9343).longitude(30.3351).build();
        locationRepository.save(spb);

        Location london = Location.builder().name("London").country("United Kingdom").latitude(51.5074).longitude(-0.1278).build();
        locationRepository.save(london);

        Location newYork = Location.builder().name("New York").country("United States").latitude(40.7128).longitude(-74.0060).build();
        locationRepository.save(newYork);

        weatherRecordRepository.save(WeatherRecord.builder().location(moscow).temperature(15.5).humidity(65.0).pressure(1013.2).windSpeed(4.2).conditionText("Partly Cloudy").recordedAt(LocalDateTime.now().minusHours(1)).build());
        weatherRecordRepository.save(WeatherRecord.builder().location(spb).temperature(12.0).humidity(80.0).pressure(1008.5).windSpeed(6.5).conditionText("Rainy").recordedAt(LocalDateTime.now().minusHours(1)).build());
        weatherRecordRepository.save(WeatherRecord.builder().location(london).temperature(18.2).humidity(70.0).pressure(1015.0).windSpeed(3.8).conditionText("Sunny").recordedAt(LocalDateTime.now().minusHours(1)).build());

        weatherForecastRepository.save(WeatherForecast.builder().location(moscow).forecastDate(LocalDate.now().plusDays(1)).tempMin(10.0).tempMax(18.0).precipitationProbability(20.0).summary("Mild with scattered clouds").build());
        weatherForecastRepository.save(WeatherForecast.builder().location(moscow).forecastDate(LocalDate.now().plusDays(2)).tempMin(8.0).tempMax(14.0).precipitationProbability(75.0).summary("Heavy rain expected").build());
        weatherForecastRepository.save(WeatherForecast.builder().location(spb).forecastDate(LocalDate.now().plusDays(1)).tempMin(9.0).tempMax(13.0).precipitationProbability(90.0).summary("Continuous rain").build());

        weatherAlertRepository.save(WeatherAlert.builder().location(spb).title("Storm Warning: High Winds").description("Gusty winds up to 22 m/s.").severity(WeatherAlert.Severity.WARNING).issuedAt(LocalDateTime.now().minusHours(3)).expiresAt(LocalDateTime.now().plusHours(24)).build());
        weatherAlertRepository.save(WeatherAlert.builder().location(moscow).title("Fog Advisory").description("Reduced visibility in morning hours.").severity(WeatherAlert.Severity.INFO).issuedAt(LocalDateTime.now().minusHours(5)).expiresAt(LocalDateTime.now().plusHours(12)).build());

        userSubscriptionRepository.save(UserSubscription.builder().user(regularUser).location(moscow).subscribedAt(LocalDateTime.now().minusDays(2)).build());
        userSubscriptionRepository.save(UserSubscription.builder().user(regularUser).location(spb).subscribedAt(LocalDateTime.now().minusDays(1)).build());

        userWeatherReportRepository.save(UserWeatherReport.builder().user(regularUser).location(moscow).temperature(16.0).conditionText("Sunny in downtown").comment("Sun just broke through clouds!").status(UserWeatherReport.ModerationStatus.PENDING).reportedAt(LocalDateTime.now().minusMinutes(30)).build());
    }
}
