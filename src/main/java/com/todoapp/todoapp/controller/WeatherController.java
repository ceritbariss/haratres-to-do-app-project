package com.todoapp.todoapp.controller;

import com.todoapp.todoapp.dto.response.weather.WeatherResponseDto;
import com.todoapp.todoapp.service.weather.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    // Girilen şehir ve ilçe parametresine göre 5 günlük hava tahminini döner.
    @GetMapping
    public ResponseEntity<WeatherResponseDto> getWeatherForecast(
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam int days
    ){
        WeatherResponseDto response = weatherService.getDaysForecast(district, city, days);
        return ResponseEntity.ok(response);
    }
}
