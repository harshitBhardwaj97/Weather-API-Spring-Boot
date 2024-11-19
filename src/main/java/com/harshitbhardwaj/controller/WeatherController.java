package com.harshitbhardwaj.controller;

import com.harshitbhardwaj.entity.WeatherResponse;
import com.harshitbhardwaj.service.WeatherService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Slf4j
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/{location}")
    @RateLimiter(name = "todays-weather")
    public ResponseEntity<?> getTodaysWeather(@PathVariable String location) {
        return ResponseEntity.ok(weatherService.getTodaysWeather(location));
    }

    @GetMapping("/{location}/range")
    @RateLimiter(name = "weather-range")
    public ResponseEntity<?> getWeatherBetweenRange(
            @PathVariable String location,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        WeatherResponse weatherResponse = weatherService.getWeatherBetweenRange(location, startDate, endDate);
        return ResponseEntity.ok(weatherResponse);
    }
}