package com.todoapp.todoapp.service.todo;

import com.todoapp.todoapp.dto.request.TodoCreateRequest;
import com.todoapp.todoapp.dto.request.TodoUpdateRequestDTO;
import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TodoService {
    Todo createTodo(TodoCreateRequest request);
    Page<Todo> getTodosForCurrentUser(int page, int size);
    void deleteTodoById(int id);
    Todo updateTodo(int id, TodoUpdateRequestDTO request);
    Page<Todo> getTodosByStatus(Status status, int page, int size);
    Page<Todo> searchTodosByTitle(String keyword, int page, int size);
    Page<Todo> getSortedTodos(String sortBy, String direction, int page, int size);
}
