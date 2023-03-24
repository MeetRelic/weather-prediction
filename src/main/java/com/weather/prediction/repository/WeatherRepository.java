package com.weather.prediction.repository;

import com.weather.prediction.models.request.WeatherData;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherRepository {
    private Map<String, List<WeatherData>> weatherDataMap;

    @PostConstruct
    private void init() {
        weatherDataMap = new HashMap<>();
    }

    public Boolean addWeather(String key, WeatherData weatherData) {
        if (weatherDataMap.containsKey(key)) {
            return weatherDataMap.get(key).add(weatherData);
        } else {
            List<WeatherData> weatherDataList = Arrays.asList(weatherData);
            return weatherDataMap.put(key, weatherDataList) != null ? true : false;
        }
    }

}
