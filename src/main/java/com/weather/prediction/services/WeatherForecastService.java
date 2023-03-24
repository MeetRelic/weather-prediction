package com.weather.prediction.services;

import com.weather.prediction.models.exceptions.WeatherException;
import com.weather.prediction.models.response.WeatherDataResponse;
import org.springframework.stereotype.Component;

public interface WeatherForecastService {
    WeatherDataResponse getWeatherReport(String location, int days) throws WeatherException;
}
