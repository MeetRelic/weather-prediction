package com.weather.prediction.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.weather.prediction.models.request.WeatherData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataResponse {
    @JsonProperty("weatherReport")
    private List<WeatherData> weatherDataList;
}
