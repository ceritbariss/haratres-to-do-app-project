package com.todoapp.todoapp.repository;

import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    // Giriş yapan kullanıcının todolarını pagination ile döndürmek için kullanılır.
    Page<Todo> findAllByUserUserName(String userName, Pageable pageable);

    // Sadece giriş yapan kullanıcıya ait olan todoyu bulmak için kullanılır.
    Optional<Todo> findByIdAndUserUserName(int id, String userName);

    // Giriş yapan kullanıcının todolarını statuslerine göre getirmek için kullanılır.
    Page<Todo> findAllByUserUserNameAndStatus(String userName, Status status, Pageable pageable);

    // Giriş yapan kullanıcının todolarındaki girilen keyworde sahip todoları bulur. Büyük/Küçük harf duyarsız.
    Page<Todo> findAllByUserUserNameAndTitleContainingIgnoreCase(String userName, String keyword, Pageable pageable);

    // Kullanıcının todolarını istediği (oluşturulma,güncelleme,bitiş) sıralamaya göre sıralayarak getirir.
    List<Todo> findAllByUserUserName(String userName, Sort sort);
}
