package com.todoapp.todoapp.repository;

import com.todoapp.todoapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
        Optional<User> findByUserName(String userName);

        Optional<User> findByEmail(String email);

        boolean existsByUserName(String username);

        boolean existsByEmail(String email);
}
