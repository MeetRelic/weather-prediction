package com.weather.prediction.models.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {
    private LocalDate date;
    private Float highTemperature;
    private Float lowTemperature;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String suggestion;

}
