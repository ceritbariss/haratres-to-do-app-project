package com.todoapp.todoapp.service.user;

import com.todoapp.todoapp.dto.RegisterRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void registerUser(RegisterRequestDto request);
}
