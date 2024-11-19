package com.harshitbhardwaj.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DayResponse implements Serializable {
    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty("tempmax")
    private double maxTemp;

    @JsonProperty("tempmin")
    private double minTemp;

    @JsonProperty("humidity")
    private double humidity;

    @JsonProperty("conditions")
    private String weatherConditions;

    @JsonProperty("description")
    private String weatherDescription;
}
