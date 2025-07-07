package com.todoapp.todoapp.controller;

import com.todoapp.todoapp.dto.request.TodoCreateRequest;
import com.todoapp.todoapp.dto.request.TodoUpdateRequestDTO;
import com.todoapp.todoapp.dto.response.TodoResponseDTO;
import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.enums.Status;
import com.todoapp.todoapp.service.todo.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PatchExchange;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private TodoService todoService;
    private ModelMapper modelMapper;

    @Autowired
    public TodoController(TodoService todoService, ModelMapper modelMapper) {
        this.todoService = todoService;
        this.modelMapper = modelMapper;
    }

    /* GET */

    // Kullanıcının bütün todolarını döndürür.
    @GetMapping
    public ResponseEntity<List<TodoResponseDTO>> getTodosForCurrentUser(){
        List<Todo> todos = todoService.getTodosForCurrentUser();

        List<TodoResponseDTO> response = todos.stream()
                .map(todo -> modelMapper.map(todo, TodoResponseDTO.class))
                .toList();

        return ResponseEntity.ok(response);
    }

    // Kullanıcının todolarını istediği statuslarına göre döndürür.
    @GetMapping("/status")
    public ResponseEntity<List<TodoResponseDTO>> getTodosByStatus(@RequestParam Status status){
        List<Todo> todos = todoService.getTodosByStatus(status);

        List<TodoResponseDTO> response = todos.stream()
                .map(todo -> modelMapper.map(todo, TodoResponseDTO.class))
                .toList();

        return ResponseEntity.ok(response);
    }

    // Kullanıcının girdiği keyword hangi todoların titleın da varsa onları döndürür.
    @GetMapping("/search")
    public ResponseEntity<List<TodoResponseDTO>> searchTodosByTitle(@RequestParam String keyword){
        List<Todo> todos = todoService.searchTodosByTitle(keyword);

        List<TodoResponseDTO> response = todos.stream()
                .map(todo -> modelMapper.map(todo, TodoResponseDTO.class))
                .toList();

        return ResponseEntity.ok(response);
    }

    // Kullanıcının istediği sıralama türüne göre todoları sıralı bir şekilde döndürür.
    @GetMapping("/sorted")
    public ResponseEntity<List<TodoResponseDTO>> getSortedTodos(
        @RequestParam(defaultValue = "createdDate") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ){
        List<Todo> todos = todoService.getSortedTodos(sortBy, direction);

        List<TodoResponseDTO> response = todos.stream()
                .map(todo -> modelMapper.map(todo, TodoResponseDTO.class))
                .toList();

        return ResponseEntity.ok(response);
    }

    /* POST */

    // Yeni todos oluşturmak için kullanılır.
    @PostMapping("/create")
    public ResponseEntity<TodoResponseDTO> createTodo(@RequestBody TodoCreateRequest request) {
        Todo createdTodo = todoService.createTodo(request);

        TodoResponseDTO responseDTO = modelMapper.map(createdTodo, TodoResponseDTO.class);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /* DELETE */

    // Id si verilen todoyu silmek için kullanılır.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id){
        todoService.deleteTodoById(id);

        return ResponseEntity.noContent().build();
    }

    /* PATCH */

    // Id si verilen todoyu güncellemek için kullanılır.
    @PatchMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@PathVariable int id, @RequestBody TodoUpdateRequestDTO request){
        Todo updated = todoService.updateTodo(id, request);

        TodoResponseDTO response = modelMapper.map(updated, TodoResponseDTO.class);

        return ResponseEntity.ok(response);
    }
}
