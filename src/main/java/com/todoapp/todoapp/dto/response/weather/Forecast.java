package com.todoapp.todoapp.dto.response.weather;

import java.util.List;

public class Forecast {

    private List<ForecastDay> forecastday;

    public List<ForecastDay> getForecastday() {
        return forecastday;
    }

    public void setForecastday(List<ForecastDay> forecastday) {
        this.forecastday = forecastday;
    }
}
