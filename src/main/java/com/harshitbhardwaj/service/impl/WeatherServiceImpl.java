package com.harshitbhardwaj.service.impl;

import com.harshitbhardwaj.entity.WeatherResponse;
import com.harshitbhardwaj.exception.ApplicationException;
import com.harshitbhardwaj.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {
    private final RestTemplate restTemplate;
    private final SimpleDateFormat simpleDateFormat;

    private final String BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";

    @Value("${weather.api.key}")
    private String apiKey;

    @Override
    @Cacheable(value = "weatherCache", key = "#location", cacheManager = "cacheManager")
    public WeatherResponse getTodaysWeather(String location) {
        log.info("####### getTodaysWeather endpoint called #######");
        String query = getQueryForToday(location);
        var todaysWeatherResponse = restTemplate.getForObject(query, WeatherResponse.class);
        log.info("Weather Response for today: {}", todaysWeatherResponse);
        return todaysWeatherResponse;
    }

    @Override
    @Cacheable(value = "weatherRangeCache", key = "#location + #startDate + #endDate", cacheManager = "cacheManager")
    public WeatherResponse getWeatherBetweenRange(String location, String startDate, String endDate) {
        log.info("####### getWeatherBetweenRange endpoint called #######");

        // First perform validations on dates
        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            log.error("####### startDate or endDate or both are invalid #######");
            throw new ApplicationException("Pass a valid date in YYYY-MM-DD format.", HttpStatus.BAD_REQUEST);
        }
        if (!isStartBeforeEnd(startDate, endDate)) {
            log.error("####### startDate cannot be equal or after endDate or both are invalid #######");
            throw new ApplicationException("End date cannot come before start date.", HttpStatus.BAD_REQUEST);
        }
        // Generate the query string
        String query = getQueryForRange(location, startDate, endDate);

        // Call the external weather API and return the response
        var weatherRangeResponse = restTemplate.getForObject(query, WeatherResponse.class);
        weatherRangeResponse.setDescriptionOfWeather("No description available for range type response.");
        log.info("####### getWeatherBetweenRange call successful #######");
        return weatherRangeResponse;
    }

    private String getQueryForToday(String location) {
        var today = getTodaysDate();
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .pathSegment(location, today)
                .queryParam("unitGroup", "metric")
                .queryParam("key", apiKey)
                .queryParam("contentType", "json")
                .toUriString();
        /*
        return String.format("%s/%s/%s?unitGroup=metric&key=%s&contentType=json"
                , BASE_URL, location, today, API_KEY);
        */
    }

    private String getQueryForRange(String location, String startDate, String endDate) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .pathSegment(location, startDate, endDate)
                .queryParam("unitGroup", "metric")
                .queryParam("key", apiKey)
                .queryParam("contentType", "json")
                .toUriString();
        /*
        return String.format("%s/%s/%s/%s?unitGroup=metric&key=%s&contentType=json"
                , BASE_URL, location, startDate, endDate, API_KEY);
        */
    }

    private String getTodaysDate() {
        return simpleDateFormat.format(new Date());
    }

    private boolean isValidDate(String date) {
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(date); // Try to parse the date string
            return true;
        } catch (ParseException e) {
            return false;  // If parsing fails, it's an invalid date
        }
    }

    private boolean isStartBeforeEnd(String startDate, String endDate) {
        try {
            Date start = simpleDateFormat.parse(startDate);
            Date end = simpleDateFormat.parse(endDate);
            return start.before(end);
        } catch (ParseException e) {
            log.error("Error parsing dates for comparison: startDate={}, endDate={}", startDate, endDate, e);
            return false;
        }
    }
}
