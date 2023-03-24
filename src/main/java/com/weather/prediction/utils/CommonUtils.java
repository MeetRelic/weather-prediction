package com.weather.prediction.utils;

public class CommonUtils {

    public static final String RAIN_SUGGESTION = "Carry umbrella";
    public static final String SUMMER_SUGGESTION = "Use sunscreen lotion";
    public static final String PLEASANT_SUGGESTION = "Weather is pleasant";
    public static final String WINTER_SUGGESTION = "Weather has low temperature , carry warmers";
    public static final String WIND_SUGGESTION = "It’s too windy, watch out!";
    public static final String STORM_SUGGESTION = "Don’t step out! A Storm is brewing!";

    public static float convertKToCelsius(String temperature) {
        float kelvin = Float.parseFloat(temperature);
        return Math.round(kelvin - 273.15F);
    }

}
