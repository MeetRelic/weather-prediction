package com.weather.prediction.controller;

import com.weather.prediction.models.request.Users;
import com.weather.prediction.models.request.AuthRequest;
import com.weather.prediction.models.response.HttpApiResponse;
import com.weather.prediction.models.response.WeatherDataResponse;
import com.weather.prediction.repository.UserRepository;
import com.weather.prediction.services.WeatherForecastService;
import com.weather.prediction.services.impl.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@Tag(name = "Weather Prediction Controller")
@Slf4j
public class WeatherPredictionController {

    @Autowired
    private WeatherForecastService weatherForecastService;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Weather Prediction for Next 3 days", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<HttpApiResponse<WeatherDataResponse>> weatherDataController(
            @RequestParam("location") String location,
            @RequestParam(value = "days", defaultValue = "3", required = false) int days) {
        if (StringUtils.isBlank(location)) {
            log.warn("[weatherDataController] Bad Request some params null or incorrect location = {} ", location);
            return new ResponseEntity<>(new HttpApiResponse<>(false, null, HttpStatus.BAD_REQUEST.getReasonPhrase()),
                    HttpStatus.BAD_REQUEST);
        }

        log.info("[weatherDataController] Request called with location = {} , days = {} ", location, days);
        WeatherDataResponse weatherDataResponse;
        try {
            weatherDataResponse = weatherForecastService.getWeatherReport(location, days);
            return new ResponseEntity<>(new HttpApiResponse<>(true, weatherDataResponse), HttpStatus.OK);
        } catch (Exception ex) {
            log.error("ERROR : [weatherDataController] Exception occurred in processing weather report ", ex);
            return new ResponseEntity<>(
                    new HttpApiResponse<>(false, null, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/auth" ,  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(authRequest.getUsername());
                return new ResponseEntity<>(jwt, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw e;
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/user" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> authenticate(@RequestBody Users user) {
        Users userAdded = userRepository.addUser(user);
        if (Objects.isNull(userAdded)) {
            return new ResponseEntity<>("User added Successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Failed to add User , User already exists", HttpStatus.ALREADY_REPORTED);
    }
}
