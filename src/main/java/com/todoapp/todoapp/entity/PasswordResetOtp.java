package com.todoapp.todoapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_otp")
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "otp")
    private String otp;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordResetOtp() {
    }

    public PasswordResetOtp(String otp, LocalDateTime expirationDate, User user) {
        this.otp = otp;
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "PasswordResetOtp{" +
                "id=" + id +
                ", otp='" + otp + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
