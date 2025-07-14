package com.todoapp.todoapp.dto.request;

import com.todoapp.todoapp.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TodoUpdateRequestDto {

    private String title;

    @Size(min = 10, message = "Description alanı en az 10 karakter olmalı.")
    private String description;

    @FutureOrPresent(message = "Son teslim tarihi geçmiş olamaz.")
    private LocalDateTime dueDate;

    private Status status;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
