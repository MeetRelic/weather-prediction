package com.weather.prediction.services.impl;

import com.weather.prediction.configs.WeatherConfiguration;
import com.weather.prediction.models.request.WeatherData;
import com.weather.prediction.models.request.WeatherList;
import com.weather.prediction.models.request.Wind;
import com.weather.prediction.models.exceptions.WeatherException;
import com.weather.prediction.models.response.ForecastResponseBody;
import com.weather.prediction.models.response.WeatherDataResponse;
import com.weather.prediction.repository.WeatherRepository;
import com.weather.prediction.services.WeatherForecastService;
import com.weather.prediction.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
@Slf4j
public class WeatherForecastServiceImpl implements WeatherForecastService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private WeatherConfiguration weatherConfiguration;

    private String getWeatherApiUri(String location, int days) {
        int count = 9 * days;
        String uriString = UriComponentsBuilder.fromUriString(weatherConfiguration.getBaseUrl())
                .queryParam("q", location).queryParam("appid", weatherConfiguration.getApikey())
                .queryParam("cnt", count).build().toUriString();
        return uriString;
    }

    private String getSuggestion(WeatherList weather) {
        Wind wind = weather.getWind();
        String suggestion;
        Float currTemp = CommonUtils.convertKToCelsius(weather.getMain().getFeels_like().toString());
        if (wind.getSpeed() > 10) {
            suggestion = wind.getSpeed() >= 12.5 ? CommonUtils.STORM_SUGGESTION : CommonUtils.WIND_SUGGESTION;
        } else {
            if (weather.getRain() != null) {
                suggestion = CommonUtils.RAIN_SUGGESTION;
            } else if (currTemp >= 40) {
                suggestion = CommonUtils.SUMMER_SUGGESTION;
            } else if (currTemp < 11) {
                suggestion = CommonUtils.WINTER_SUGGESTION;
            } else {
                suggestion = CommonUtils.PLEASANT_SUGGESTION;
            }
        }
        return suggestion;
    }

    private List<WeatherData> processWeatherResponse(ForecastResponseBody responseBody, int days) {
        ArrayList<WeatherList> weatherLists = responseBody.getList();
        List<WeatherData> weatherDataList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (WeatherList weather : weatherLists) {
            LocalDate currDate = LocalDateTime
                    .ofInstant(Instant.ofEpochSecond(weather.getDt()), TimeZone.getDefault().toZoneId()).toLocalDate();
            Integer currIndex = (int) ChronoUnit.DAYS.between(today, currDate);
            if (currIndex > 0) {
                if (currIndex > days) {
                    return weatherDataList;
                }
                WeatherData weatherData = new WeatherData();
                WeatherList.Main weatherMain = weather.getMain();
                weatherData.setDate(currDate);
                weatherData.setLowTemperature(CommonUtils.convertKToCelsius(weatherMain.getTemp_min().toString()));
                weatherData.setHighTemperature(CommonUtils.convertKToCelsius(weatherMain.getTemp_max().toString()));
                weatherData.setSuggestion(getSuggestion(weather));
                if (weatherDataList.size() >= currIndex) {
                    Float minTemp = weatherDataList.get(currIndex - 1).getLowTemperature();
                    Float maxTemp = weatherDataList.get(currIndex - 1).getHighTemperature();

                    if (minTemp > weatherData.getLowTemperature()) {
                        weatherDataList.get(currIndex - 1).setLowTemperature(weatherData.getLowTemperature());

                    }
                    if (maxTemp < weatherData.getHighTemperature()) {
                        weatherDataList.get(currIndex - 1).setHighTemperature(weatherData.getHighTemperature());
                    }
                } else {
                    weatherDataList.add(weatherData);
                }
            }
        }
        return weatherDataList;
    }

    @Override
    public WeatherDataResponse getWeatherReport(String location, int days) throws WeatherException {
        WeatherDataResponse weatherDataResponse = new WeatherDataResponse();
        log.info("[getWeatherReport] Making weather report request for location = {} ", location);
        if (weatherRepository.getWeatherDataMap().containsKey(location)) {
            weatherDataResponse.setWeatherDataList(weatherRepository.getWeatherDataMap().get(location));
            return weatherDataResponse;
        }
        try {
            ResponseEntity<ForecastResponseBody> response = restTemplate.getForEntity(getWeatherApiUri(location, days),
                    ForecastResponseBody.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<WeatherData> weatherDataList = processWeatherResponse(response.getBody(), days);
                weatherDataResponse.setWeatherDataList(weatherDataList);
                weatherRepository.getWeatherDataMap().put(location, weatherDataList);
                return weatherDataResponse;
            } else {
                log.error("ERROR : [getWeatherReport] weather api failed to send response , errorCode = {} ",
                        response.getStatusCode());
                throw new WeatherException("Connection issue with public api , and data not cached earlier");
            }

        } catch (Exception ex) {
            log.error("ERROR: [getWeatherReport] Exception occurred - ", ex);
            throw new WeatherException("HttpConnection issues with Public api.");
        }

    }
}
