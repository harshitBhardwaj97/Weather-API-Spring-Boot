package com.harshitbhardwaj.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTodaysWeather() throws Exception {
        String location = "delhi";

        mockMvc.perform(get("/api/weather/{location}", location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(1)))
                .andExpect(jsonPath("$.resolvedAddress").value("Delhi, DL, India"))
                .andExpect(jsonPath("$.latitude").value(28.6341))
                .andExpect(jsonPath("$.longitude").value(77.2169));
    }

    @Test
    public void testGetWeatherBetweenRange() throws Exception {
        String location = "delhi";
        String startDate = "2024-10-01";
        String endDate = "2024-10-03";

        mockMvc.perform(get("/api/weather/{location}/range", location)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.days", hasSize(3)))
                .andExpect(jsonPath("$.resolvedAddress").value("Delhi, DL, India"))
                .andExpect(jsonPath("$.latitude").value(28.6341))
                .andExpect(jsonPath("$.longitude").value(77.2169));
    }

    @Test
    public void testInvalidEndpoint() throws Exception {

        mockMvc.perform(get("/invalid-endpoint"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString("No static resource invalid-endpoint.")));
    }

    @Test
    public void testInvalidLocation() throws Exception {
        String invalidLocation = "abcdef";  // Invalid location

        mockMvc.perform(get("/api/weather/{location}", invalidLocation))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString("Bad API Request:Invalid location parameter value.")));

    }

    @Test
    public void testInvalidDateFormatWithIncorrectMonth() throws Exception {
        String location = "delhi";
        String startDate = "2024-13-01"; // Invalid month (13)
        String endDate = "2024-10-03";

        mockMvc.perform(get("/api/weather/{location}/range", location)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Pass a valid date in YYYY-MM-DD format."));
    }

    @Test
    public void testInvalidDateFormat() throws Exception {
        String location = "delhi";
        String startDate = "abc"; // Invalid date format
        String endDate = "2024-10-03";

        mockMvc.perform(get("/api/weather/{location}/range", location)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Pass a valid date in YYYY-MM-DD format."));
    }

    @Test
    public void testInvalidRange() throws Exception {
        String location = "delhi";
        String startDate = "2024-10-03";
        String endDate = "2024-01-03"; // End date comes before start date

        mockMvc.perform(get("/api/weather/{location}/range", location)
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("End date cannot come before start date."));
    }
}
