package com.todoapp.todoapp.service.todo.impl;

import com.todoapp.todoapp.dto.request.TodoCreateRequestDto;
import com.todoapp.todoapp.dto.request.TodoUpdateRequestDto;
import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.entity.User;
import com.todoapp.todoapp.enums.Status;
import com.todoapp.todoapp.repository.TodoRepository;
import com.todoapp.todoapp.repository.UserRepository;
import com.todoapp.todoapp.service.todo.TodoService;
import com.todoapp.todoapp.util.UserUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TodoServiceImpl implements TodoService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final UserUtil userUtil;

    @Autowired
    public TodoServiceImpl(UserRepository userRepository, TodoRepository todoRepository, UserUtil userUtil) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.userUtil = userUtil;
    }


    // Kullanıcının yeni todos oluşturmasını sağlar.
    @Override
    @Transactional
    public Todo createTodo(TodoCreateRequestDto request) {
        String username = userUtil.getCurrentUsername();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());
        todo.setStatus(Status.CREATED);
        todo.setCreatedDate(LocalDateTime.now());
        todo.setUpdateDate(LocalDateTime.now());
        todo.setUser(user);

        return todoRepository.save(todo);
    }

    // Giriş yapan kullanıcının todolarını getirmek için.
    @Override
    public Page<Todo> getTodosForCurrentUser(int page, int size) {
        String username = userUtil.getCurrentUsername();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        return todoRepository.findAllByUserUserName(username, pageable);
    }

    // Giriş yapmış olan kullanıcının gönderdiği id ye sahip todoyu silmek için kulanılır.
    @Override
    @Transactional
    public void deleteTodoById(int id) {
        String currentUsername = userUtil.getCurrentUsername();

        Todo todo = todoRepository.findByIdAndUserUserName(id, currentUsername)
                .orElseThrow(() -> new RuntimeException("Bu todo size ait değil veya böyle bir todo yok."));

        todoRepository.delete(todo);
    }

    // Kullanıcının id numarasını verdiği todoyu güncellemek için kullanılır.
    @Override
    public Todo updateTodo(int id, TodoUpdateRequestDto request) {
        String username = userUtil.getCurrentUsername();

        Todo todo = todoRepository.findByIdAndUserUserName(id, username)
                .orElseThrow(() -> new RuntimeException("Bu todo size ait değil veya böyle bir todo yok."));

        if (StringUtils.isNotBlank(request.getTitle())) {
            todo.setTitle(request.getTitle());
        }

        if (StringUtils.isNotBlank(request.getDescription())) {
            todo.setDescription(request.getDescription());
        }

        if (request.getDueDate() != null) {
            todo.setDueDate(request.getDueDate());
        }

        if (request.getStatus() != null) {
            todo.setStatus(request.getStatus());
        }

        todo.setUpdateDate(LocalDateTime.now());

        return todoRepository.save(todo);
    }

    // Kullanıcının istediği statuslere sahip todoları göstermek için kullanılır.
    @Override
    public Page<Todo> getTodosByStatus(Status status, int page, int size) {
        String username = userUtil.getCurrentUsername();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        return todoRepository.findAllByUserUserNameAndStatus(username, status, pageable);
    }

    // Kullanıcının verdiği keywordün bulunduğu title a sahip todoları döndürmek için kullanılır.
    @Override
    public Page<Todo> searchTodosByTitle(String keyword, int page, int size) {
        String username = userUtil.getCurrentUsername();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        return todoRepository.findAllByUserUserNameAndTitleContainingIgnoreCase(username, keyword, pageable);
    }

    // İstenilen sıralama türüne göre kullanıcının todolarını sıralı bir şekilde getirmeye yarar.
    @Override
    public Page<Todo> getSortedTodos(String sortBy, String direction, int page, int size) {
        String username = userUtil.getCurrentUsername();

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        return todoRepository.findAllByUserUserName(username, pageable);
    }
}
