package com.weather.prediction.models.response;

import com.weather.prediction.models.request.City;
import com.weather.prediction.models.request.WeatherList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastResponseBody {

    private String cod;
    private int message;
    private int cnt;
    private ArrayList<WeatherList> list;
    private City city;

}
