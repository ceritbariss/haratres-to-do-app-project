package com.todoapp.todoapp.service.todo;

import com.todoapp.todoapp.dto.request.TodoCreateRequest;
import com.todoapp.todoapp.dto.request.TodoUpdateRequestDTO;
import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.enums.Status;

import java.util.List;

public interface TodoService {
    Todo createTodo(TodoCreateRequest request);
    List<Todo> getTodosForCurrentUser();
    void deleteTodoById(int id);
    Todo updateTodo(int id, TodoUpdateRequestDTO request);
    List<Todo> getTodosByStatus(Status status);
    List<Todo> searchTodosByTitle(String keyword);
    List<Todo> getSortedTodos(String sortBy, String direction);
}
