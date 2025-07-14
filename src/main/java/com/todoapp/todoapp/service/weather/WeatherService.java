package com.todoapp.todoapp.service.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.todoapp.dto.response.weather.WeatherResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public WeatherService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherResponseDto getDaysForecast(String district, String city, int days){

        try {
            String query = String.format("%s,%s", district, city);
            String url = String.format(
                    "https://api.weatherapi.com/v1/forecast.json?key=%s&q=%s&days=%d&lang=tr",
                    apiKey, query, days
            );

            String response = restTemplate.getForObject(url, String.class);

            WeatherResponseDto weatherResponseDto = objectMapper.readValue(response, WeatherResponseDto.class);

            return weatherResponseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON oluşturulurken hata oldu: " + e.getMessage(), e);
        }

    }
}
