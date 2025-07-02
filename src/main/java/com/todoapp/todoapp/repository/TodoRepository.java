package com.todoapp.todoapp.repository;

import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.enums.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findAllByUserUserName(String userName);

    // Sadece giriş yapan kullanıcıya ait olan todoyu bulmak için kullanılır.
    Optional<Todo> findByIdAndUserUserName(int id, String userName);

    // Giriş yapan kullanıcının todolarını statuslerine göre getirmek için kullanılır.
    List<Todo> findAllByUserUserNameAndStatus(String userName, Status status);

    // Giriş yapan kullanıcının todolarındaki girilen keyworde sahip todoları bulur. Büyük/Küçük harf duyarsız.
    List<Todo> findAllByUserUserNameAndTitleContainingIgnoreCase(String userName, String keyword);

    // Kullanıcının todolarını istediği (oluşturulma,güncelleme,bitiş) sıralamaya göre sıralayarak getirir.
    List<Todo> findAllByUserUserName(String userName, Sort sort);
}
