package com.todoapp.todoapp.service.todo.impl;

import com.todoapp.todoapp.dto.request.TodoCreateRequest;
import com.todoapp.todoapp.dto.request.TodoUpdateRequestDTO;
import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.entity.User;
import com.todoapp.todoapp.enums.Status;
import com.todoapp.todoapp.repository.TodoRepository;
import com.todoapp.todoapp.repository.UserRepository;
import com.todoapp.todoapp.service.todo.TodoService;
import com.todoapp.todoapp.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }


    // Kullanıcının yeni todos oluşturmasını sağlar.
    @Override
    @Transactional
    public Todo createTodo(TodoCreateRequest request) {
        String username = UserUtil.getCurrentUsername();

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
    public List<Todo> getTodosForCurrentUser() {
        String username = UserUtil.getCurrentUsername();

        return todoRepository.findAllByUserUserName(username);
    }

    // Giriş yapmış olan kullanıcının gönderdiği id ye sahip todoyu silmek için kulanılır.
    @Override
    @Transactional
    public void deleteTodoById(int id) {
        String currentUsername = UserUtil.getCurrentUsername();

        Todo todo = todoRepository.findByIdAndUserUserName(id, currentUsername)
                .orElseThrow(() -> new AccessDeniedException("Bu todo size ait değil veya böyle bir todo yok."));

        todoRepository.delete(todo);
    }

    // Kullanıcının id numarasını verdiği todoyu güncellemek için kullanılır.
    @Override
    public Todo updateTodo(int id, TodoUpdateRequestDTO request) {
        String username = UserUtil.getCurrentUsername();

        Todo todo = todoRepository.findByIdAndUserUserName(id, username)
                .orElseThrow(() -> new AccessDeniedException("Bu todo size ait değil veya böyle bir todo yok."));

        if (request.getTitle() != null) {
            todo.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
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
    public List<Todo> getTodosByStatus(Status status) {
        String username = UserUtil.getCurrentUsername();
        return todoRepository.findAllByUserUserNameAndStatus(username, status);
    }

    // Kullanıcının verdiği keywordün bulunduğu title a sahip todoları döndürmek için kullanılır.
    @Override
    public List<Todo> searchTodosByTitle(String keyword) {
        String username = UserUtil.getCurrentUsername();

        return todoRepository.findAllByUserUserNameAndTitleContainingIgnoreCase(username, keyword);
    }

    // İstenilen sıralama türüne göre kullanıcının todolarını sıralı bir şekilde getirmeye yarar.
    @Override
    public List<Todo> getSortedTodos(String sortBy, String direction) {
        String username = UserUtil.getCurrentUsername();

        // Eğer geçerli bir değer girilmezse default olarak "createdDate" seçilir.
        List<String> allowedFields = List.of("createdDate", "updateDate", "dueDate");
        if (!allowedFields.contains(sortBy)){
            sortBy = "createdDate";
        }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return todoRepository.findAllByUserUserName(username, sort);
    }
}
