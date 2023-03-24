package com.weather.prediction.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherList {

    private Integer dt;
    private Main main;
    private ArrayList<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private Integer visibility;
    private Double pop;
    private Sys sys;
    private String dt_txt;
    private Rain rain;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Clouds {
        private Integer all;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Main {
        private Double temp;
        private Double feels_like;
        private Double temp_min;
        private Double temp_max;
        private Integer pressure;
        private Integer sea_level;
        private Integer grnd_level;
        private Integer humidity;
        private Double temp_kf;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Sys {
        private String pod;
    }
}
