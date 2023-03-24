package com.weather.prediction.models.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private Integer id;
    private String name;
    private Coord coord;
    private String country;
    private Integer population;
    private Integer timezone;
    private Integer sunrise;
    private Integer sunset;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class Coord {
        private Double lat;
        private Double lon;
    }
}
