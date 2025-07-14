package com.todoapp.todoapp.service.user.impl;

import com.todoapp.todoapp.dto.request.RegisterRequestDto;
import com.todoapp.todoapp.dto.request.UserUpdateRequestDto;
import com.todoapp.todoapp.repository.UserRepository;
import com.todoapp.todoapp.security.JwtUtil;
import com.todoapp.todoapp.service.user.UserService;
import com.todoapp.todoapp.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.todoapp.todoapp.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserUtil userUtil;

    @Autowired
    UserServiceImpl(UserRepository theUserRepository, PasswordEncoder thePasswordEncoder,
                    JwtUtil theJwtUtil, UserUtil theUserUtil){
        userRepository = theUserRepository;
        passwordEncoder = thePasswordEncoder;
        jwtUtil = theJwtUtil;
        userUtil = theUserUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                Collections.emptyList()
        );
    }

    @Override
    public void registerUser(RegisterRequestDto request) {

        if (userRepository.findByUserName(request.getUserName()).isPresent()){
            throw new RuntimeException("Bu kullanıcı adı kullanılıyor!");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu e-posta adresi zaten kayıtlı.");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setBirthDate(request.getBirthDate());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public String updateCurrentUser(UserUpdateRequestDto request) {
        String currentUsername = userUtil.getCurrentUsername();
        User user = userRepository.findByUserName(currentUsername)
                .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

        boolean usernameChanged = userUtil.updateUserFields(request, user);

        if (usernameChanged){
            return jwtUtil.generateToken(user.getUserName());
        }
        return null;
    }

    @Override
    public void deleteCurrentUser() {
        String currentUsername = userUtil.getCurrentUsername();

        User user = userRepository.findByUserName(currentUsername)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        userRepository.delete(user);
    }
}
