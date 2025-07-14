package com.todoapp.todoapp.util;

import com.todoapp.todoapp.dto.request.UserUpdateRequestDto;
import com.todoapp.todoapp.entity.User;
import com.todoapp.todoapp.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserUtil {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserUtil(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String getCurrentUsername(){ // Giriş yapmış olan kullanıcının username'ini alır.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }


    public boolean updateUserFields(UserUpdateRequestDto request, User user)
    {
        updateNotBlankFields(user, request.getFirstName(), "firstName");
        updateNotBlankFields(user, request.getLastName(), "lastName");
        updateNotBlankFields(user, request.getGender(), "gender");
        updateNotBlankFields(user, request.getPhoneNumber(), "phoneNumber");
        updateNotBlankFields(user, request.getPassword(), "password");

        updateNullableFields(user, request.getBirthDate(), "birthDate");

        boolean usernameChanged = updateUniqueFields(user, request.getUsername(), "username");
        updateUniqueFields(user, request.getEmail(), "email");

        userRepository.save(user);

        if (usernameChanged){
            return true;
        }
        return false;
    }

    private void updateNotBlankFields(User user, String temp, String fieldName){
        if (temp != null){
            if (StringUtils.isBlank(temp)){
                throw new RuntimeException("Lütfen geçerli bir değer giriniz!");
            }
            switch (fieldName){
                case "firstName" -> user.setFirstName(temp);
                case "lastName" -> user.setLastName(temp);
                case "gender" -> user.setGender(temp);
                case "phoneNumber" -> user.setPhoneNumber(temp);
                case "password" -> user.setPassword(passwordEncoder.encode(temp));
            }
        }
    }

    private void updateNullableFields(User user, Object temp, String fieldName){
        if (temp != null){
            switch (fieldName){
                case "birthDate" -> user.setBirthDate((LocalDate) temp);
            }
        }
    }

    private boolean updateUniqueFields(User user, String temp, String fieldName){
        if (temp != null){
            if (StringUtils.isBlank(temp)){
                throw new RuntimeException("Lütfen geçerli bir değer giriniz!");
            }
            switch (fieldName){
                case "username" ->
                {
                    if (!(temp.equals(user.getUserName()) && userRepository.existsByUserName(temp))){
                        user.setUserName(temp);
                        return true;
                    } else {
                        throw new RuntimeException("Girdiğiniz kullanıcı adı başkası tarafından kullanılıyor yada şuanda kullandığınız kullanıcı adına eşit!");
                    }
                }

                case "email" ->
                {
                    if (!(temp.equals(user.getEmail()) && userRepository.existsByEmail(temp))){
                        user.setEmail(temp);
                    } else {
                        throw new RuntimeException("Girdiğiniz email zaten kayıtlı!");
                    }
                }
            }
        }
        return false;
    }
}
