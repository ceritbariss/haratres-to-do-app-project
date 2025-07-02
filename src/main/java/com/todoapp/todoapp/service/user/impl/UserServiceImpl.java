package com.todoapp.todoapp.service.user.impl;

import com.todoapp.todoapp.dto.request.RegisterRequestDto;
import com.todoapp.todoapp.repository.UserRepository;
import com.todoapp.todoapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import com.todoapp.todoapp.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService, UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserServiceImpl(UserRepository theUserRepository, PasswordEncoder thePasswordEncoder){
        userRepository = theUserRepository;
        passwordEncoder = thePasswordEncoder;
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
}
