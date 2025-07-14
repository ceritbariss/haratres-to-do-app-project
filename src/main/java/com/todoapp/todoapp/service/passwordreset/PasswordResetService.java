package com.todoapp.todoapp.service.passwordreset;

public interface PasswordResetService {

    void sendOtpToEmail(String email);

    boolean verifyOtp(String email, String otp);

    void updatePassword(String email, String otp, String newPassword);
}
