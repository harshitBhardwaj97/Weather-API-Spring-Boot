package com.harshitbhardwaj.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WeatherResponse implements Serializable {
    private double latitude;
    private double longitude;
    private String resolvedAddress;

    @JsonProperty("timezone")
    private String timeZone;

    @JsonProperty("description")
    private String descriptionOfWeather;

    @JsonProperty("days")
    private List<DayResponse> dayResponseList;
}
