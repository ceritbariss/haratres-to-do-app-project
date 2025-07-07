package com.todoapp.todoapp.controller;

import com.todoapp.todoapp.dto.request.TodoCreateRequest;
import com.todoapp.todoapp.dto.request.TodoUpdateRequestDTO;
import com.todoapp.todoapp.dto.response.TodoResponseDTO;
import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.enums.Status;
import com.todoapp.todoapp.service.todo.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PatchExchange;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Validated
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
    public ResponseEntity<Page<TodoResponseDTO>> getTodosForCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Todo> todos = todoService.getTodosForCurrentUser(page, size);

        Page<TodoResponseDTO> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDTO.class)
        );

        return ResponseEntity.ok(response);
    }

    // Kullanıcının todolarını istediği statuslarına göre döndürür.
    @GetMapping("/status")
    public ResponseEntity<Page<TodoResponseDTO>> getTodosByStatus(@RequestParam @NotBlank(message = "Status boş olamaz.") Status status,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size)
    {
        Page<Todo> todos = todoService.getTodosByStatus(status, page, size);

        Page<TodoResponseDTO> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDTO.class));

        return ResponseEntity.ok(response);
    }

    // Kullanıcının girdiği keyword hangi todoların titleın da varsa onları döndürür.
    @GetMapping("/search")
    public ResponseEntity<Page<TodoResponseDTO>> searchTodosByTitle(@RequestParam @NotBlank(message = "Anahtar kelime boş olamaz.") String keyword,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size)
    {
        Page<Todo> todos = todoService.searchTodosByTitle(keyword, page, size);

        Page<TodoResponseDTO> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDTO.class));

        return ResponseEntity.ok(response);
    }

    // Kullanıcının istediği sıralama türüne göre todoları sıralı bir şekilde döndürür.
    @GetMapping("/sorted")
    public ResponseEntity<Page<TodoResponseDTO>> getSortedTodos(
        @RequestParam(defaultValue = "createdDate") @NotBlank(message = "Created Date alanı boş olamaz.") String sortBy,
        @RequestParam(defaultValue = "asc") @NotBlank(message = "Direction alanı boş olamaz.") String direction,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Page<Todo> todos = todoService.getSortedTodos(sortBy, direction, page, size);

        Page<TodoResponseDTO> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDTO.class));

        return ResponseEntity.ok(response);
    }

    /* POST */

    // Yeni todos oluşturmak için kullanılır.
    @PostMapping("/create")
    public ResponseEntity<TodoResponseDTO> createTodo(@Valid @RequestBody TodoCreateRequest request) {
        Todo createdTodo = todoService.createTodo(request);

        TodoResponseDTO responseDTO = modelMapper.map(createdTodo, TodoResponseDTO.class);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /* DELETE */

    // Id si verilen todoyu silmek için kullanılır.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable @Min(value = 1, message = "ID 1'den küçük olamaz") int id){
        todoService.deleteTodoById(id);

        return ResponseEntity.noContent().build();
    }

    /* PATCH */

    // Id si verilen todoyu güncellemek için kullanılır.
    @PatchMapping("/{id}")
    public ResponseEntity<TodoResponseDTO> updateTodo(@PathVariable @Min(value = 1, message = "ID 1'den küçük olamaz") int id,
                                                      @Valid @RequestBody TodoUpdateRequestDTO request){
        Todo updated = todoService.updateTodo(id, request);

        TodoResponseDTO response = modelMapper.map(updated, TodoResponseDTO.class);

        return ResponseEntity.ok(response);
    }
}
