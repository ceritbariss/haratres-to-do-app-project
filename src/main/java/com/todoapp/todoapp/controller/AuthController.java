package com.todoapp.todoapp.controller;

import com.todoapp.todoapp.dto.request.*;
import com.todoapp.todoapp.dto.response.AuthResponseDto;
import com.todoapp.todoapp.security.JwtUtil;
import com.todoapp.todoapp.service.passwordreset.PasswordResetService;
import com.todoapp.todoapp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDto authRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getUserPassword()
                    )
            );

            String token = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponseDto(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Kullanıcı adı veya şifre hatalı!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto request){
        userService.registerUser(request);
        return ResponseEntity.ok("Kayıt başarılı!");
    }

    // Kullanıcı şifresini unuttuysa email adresini giriyor ve OTP gönderiliyor.
    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendOtp(@Valid @RequestBody EmailRequestDto request){
        passwordResetService.sendOtpToEmail(request.getEmail());
        return ResponseEntity.ok("OTP e-mail adresinize gönderildi!");
    }

    // Kullanıcı email adresine gelen OTP kodunu ve email adresini gönderir eşleşiyor mu kontrolü yapılır.
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody OtpVerificationRequestDto request){
        boolean isValid = passwordResetService.verifyOtp(request.getEmail(), request.getOtp());

        return isValid ? ResponseEntity.ok("OTP Doğrulandı!")
                : ResponseEntity.badRequest().body("Geçersiz veya süresi dolmuş OTP!");
    }

    // Eğer girilen OTP email adresiyle uyuşuyorsa yeni girilen şifreyle güncellenir.
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequestDto request){
        passwordResetService.updatePassword(request.getEmail(), request.getOtp(), request.getNewPassword());
        return ResponseEntity.ok("Şifreniz başarıyla güncellendi.");
    }
}
