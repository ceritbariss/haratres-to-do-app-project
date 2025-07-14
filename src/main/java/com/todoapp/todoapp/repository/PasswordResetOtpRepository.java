package com.todoapp.todoapp.repository;

import com.todoapp.todoapp.entity.PasswordResetOtp;
import com.todoapp.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Integer> {

    // Girilen email'e sahip kullanıcının OTP kaydını getirir.
    Optional<PasswordResetOtp> findByUser_Email(String email);

    // Kullanıcının önceki OTP'lerini silmek için kullanılır.
    void deleteByUser(User user);
}
