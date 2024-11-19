package com.harshitbhardwaj.service;

import com.harshitbhardwaj.entity.WeatherResponse;

public interface WeatherService {

    WeatherResponse getTodaysWeather(String location);

    WeatherResponse getWeatherBetweenRange(String location, String startDate, String endDate);
}
