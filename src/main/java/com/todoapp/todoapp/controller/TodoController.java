package com.todoapp.todoapp.controller;

import com.todoapp.todoapp.dto.request.TodoCreateRequestDto;
import com.todoapp.todoapp.dto.request.TodoUpdateRequestDto;
import com.todoapp.todoapp.dto.response.TodoResponseDto;
import com.todoapp.todoapp.entity.Todo;
import com.todoapp.todoapp.enums.Status;
import com.todoapp.todoapp.service.todo.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoController {

    private TodoService todoService;
    private ModelMapper modelMapper;


    public TodoController(TodoService todoService, ModelMapper modelMapper) {
        this.todoService = todoService;
        this.modelMapper = modelMapper;
    }

    /* GET */

    // Kullanıcının bütün todolarını döndürür.
    @GetMapping
    public ResponseEntity<Page<TodoResponseDto>> getTodosForCurrentUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Todo> todos = todoService.getTodosForCurrentUser(page, size);

        Page<TodoResponseDto> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDto.class)
        );

        return ResponseEntity.ok(response);
    }

    // Kullanıcının todolarını istediği statuslarına göre döndürür.
    @GetMapping("/status")
    public ResponseEntity<Page<TodoResponseDto>> getTodosByStatus(@RequestParam Status status,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size)
    {
        Page<Todo> todos = todoService.getTodosByStatus(status, page, size);

        Page<TodoResponseDto> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDto.class));

        return ResponseEntity.ok(response);
    }

    // Kullanıcının girdiği keyword hangi todoların titleın da varsa onları döndürür.
    @GetMapping("/search")
    public ResponseEntity<Page<TodoResponseDto>> searchTodosByTitle(@RequestParam @NotBlank(message = "Anahtar kelime boş olamaz.") String keyword,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size)
    {
        Page<Todo> todos = todoService.searchTodosByTitle(keyword, page, size);

        Page<TodoResponseDto> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDto.class));

        return ResponseEntity.ok(response);
    }

    // Kullanıcının istediği sıralama türüne göre todoları sıralı bir şekilde döndürür.
    @GetMapping("/sorted")
    public ResponseEntity<Page<TodoResponseDto>> getSortedTodos(
        @RequestParam @NotBlank(message = "Created Date alanı boş olamaz.") String sortBy,
        @RequestParam @NotBlank(message = "Direction alanı boş olamaz.") String direction,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Page<Todo> todos = todoService.getSortedTodos(sortBy, direction, page, size);

        Page<TodoResponseDto> response = todos.map(todo ->
                modelMapper.map(todo, TodoResponseDto.class));

        return ResponseEntity.ok(response);
    }

    /* POST */

    // Yeni todos oluşturmak için kullanılır.
    @PostMapping("/create")
    public ResponseEntity<TodoResponseDto> createTodo(@Valid @RequestBody TodoCreateRequestDto request) {
        Todo createdTodo = todoService.createTodo(request);

        TodoResponseDto responseDTO = modelMapper.map(createdTodo, TodoResponseDto.class);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /* DELETE */

    // Id si verilen todoyu silmek için kullanılır.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable @Min(value = 1, message = "ID 1'den küçük olamaz") int id) {
        todoService.deleteTodoById(id);

        return ResponseEntity.noContent().build();
    }

    /* PATCH */

    // Id si verilen todoyu güncellemek için kullanılır.
    @PatchMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable @Min(value = 1, message = "ID 1'den küçük olamaz") int id,
                                                      @Valid @RequestBody TodoUpdateRequestDto request){
        Todo updated = todoService.updateTodo(id, request);

        TodoResponseDto response = modelMapper.map(updated, TodoResponseDto.class);

        return ResponseEntity.ok(response);
    }
}
