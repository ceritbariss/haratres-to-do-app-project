package com.todoapp.todoapp.controller;

import com.todoapp.todoapp.dto.request.UserUpdateRequestDto;
import com.todoapp.todoapp.dto.response.AuthResponseDto;
import com.todoapp.todoapp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PatchMapping
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UserUpdateRequestDto request){
        String newToken = userService.updateCurrentUser(request);

        if (newToken != null){
            return ResponseEntity.ok(new AuthResponseDto(newToken));
        }

        return ResponseEntity.ok("Kullanıcı bilgileri güncellendi");
    }


    @DeleteMapping
    public ResponseEntity<String> deleteCurrentUser(){
        userService.deleteCurrentUser();
        return ResponseEntity.ok("Hesabınız başarıyla silindi.");
    }
}
