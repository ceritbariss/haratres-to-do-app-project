package com.todoapp.todoapp.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class TodoCreateRequestDto {

    @NotBlank(message = "Title alanı boş olamaz.")
    private String title;

    @NotBlank(message = "Description alanı boş olamaz.")
    @Size(min = 10, message = "Description alanı en az 10 karakter olmalı.")
    private String description;

    @FutureOrPresent(message = "Son teslim tarihi geçmiş bir tarih olamaz.")
    @NotNull(message = "Son teslim tarihi (dueDate) boş olamaz.")
    private LocalDateTime dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

}
