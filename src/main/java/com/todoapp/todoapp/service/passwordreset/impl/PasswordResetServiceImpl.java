package com.todoapp.todoapp.service.passwordreset.impl;

import com.todoapp.todoapp.entity.PasswordResetOtp;
import com.todoapp.todoapp.entity.User;
import com.todoapp.todoapp.repository.PasswordResetOtpRepository;
import com.todoapp.todoapp.repository.UserRepository;
import com.todoapp.todoapp.service.passwordreset.PasswordResetService;
import com.todoapp.todoapp.util.PasswordResetUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetOtpRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetUtil passwordResetUtil;

    public PasswordResetServiceImpl(PasswordResetOtpRepository otpRepository, UserRepository userRepository,
                                    PasswordEncoder passwordEncoder, PasswordResetUtil passwordResetUtil) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetUtil = passwordResetUtil;
    }

    // Eğer gönderilen email e kayıtlı kullanıcı varsa email'ine OTP kodu gönderilir.
    @Override
    @Transactional
    public void sendOtpToEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        otpRepository.deleteByUser(user);

        String otp = passwordResetUtil.generateOtp();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        PasswordResetOtp resetOtp = new PasswordResetOtp(otp, expiration, user);
        otpRepository.save(resetOtp);

        passwordResetUtil.sendEmail(user.getEmail(), otp);
    }

    // Gönderilen email ve OTP kodu eşleşiyor mu bu kontrol sağlanır.
    @Override
    public boolean verifyOtp(String email, String otp) {
        PasswordResetOtp resetOtp = otpRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Hatalı E-Mail Veya OTP Girişi Yaptınız!"));

        return resetOtp.getOtp().equals(otp) &&
                resetOtp.getExpirationDate().isAfter(LocalDateTime.now());
    }

    // Eğer OTP kodu ve Email'i uyuşuyorsa şifre güncellemesi yapılır.
    @Override
    @Transactional
    public void updatePassword(String email, String otp, String newPassword) {

        PasswordResetOtp resetOtp = otpRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("OTP bulunamadı."));

        if (!resetOtp.getOtp().equals(otp)){
            throw new RuntimeException("Geçersiz OTP!");
        }

        if (resetOtp.getExpirationDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP süresi dolmuş!");
        }

        User user = resetOtp.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpRepository.deleteByUser(user); // Kullanıldıktan sonra OTP silinir.
    }
}
