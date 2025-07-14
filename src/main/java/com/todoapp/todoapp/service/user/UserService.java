package com.todoapp.todoapp.service.user;

import com.todoapp.todoapp.dto.request.RegisterRequestDto;
import com.todoapp.todoapp.dto.request.UserUpdateRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void registerUser(RegisterRequestDto request);
    String updateCurrentUser(UserUpdateRequestDto request);
    void deleteCurrentUser();
}
