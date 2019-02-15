package io.todo.todo.controller;

import io.todo.todo.domain.dto.request.TodoRequest;
import io.todo.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="/v1/todo", produces = APPLICATION_JSON_VALUE)
public class TodoController {

	@Autowired
	private TodoService todoService;

	@GetMapping()
	public CompletableFuture<ResponseEntity> getTodos(final Principal principal) {
		return todoService
				.getTodos(principal.getName())
				.thenApply(ResponseEntity::ok);
	}

	@PostMapping()
	public CompletableFuture<ResponseEntity> addTodo(@RequestBody TodoRequest todoRequest, final Principal principal) {
		return todoService
				.addTodo(todoRequest, principal.getName())
				.thenApply(ResponseEntity::ok);
	}

	@PutMapping()
	public CompletableFuture<ResponseEntity> toggleTodo(@RequestBody TodoRequest todoRequest, final Principal principal) {
		return todoService
				.toggleTodo(todoRequest, principal.getName())
				.thenApply(ResponseEntity::ok);
	}

	@DeleteMapping()
	public CompletableFuture<ResponseEntity> deleteTodo(@RequestBody TodoRequest todoRequest, final Principal principal) {
		return todoService
				.deleteTodo(todoRequest, principal.getName())
				.thenApply(ResponseEntity::ok);
	}

}
