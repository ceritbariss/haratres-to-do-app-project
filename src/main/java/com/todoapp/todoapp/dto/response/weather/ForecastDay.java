package com.todoapp.todoapp.dto.response.weather;

import java.time.LocalDate;

public class ForecastDay {

    private LocalDate date;
    private Day day;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
