package io.todo.todo.service;

import io.todo.todo.domain.dto.request.TodoRequest;
import io.todo.todo.domain.entity.Todo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TodoService {

	CompletableFuture<List<Todo>> getTodos(final String userId);

	CompletableFuture<Optional<Todo>> addTodo(final TodoRequest todoRequest, final String userId);

	CompletableFuture<Optional<Todo>> deleteTodo(final TodoRequest todoRequest, final String userId);

	CompletableFuture<Optional<Todo>> toggleTodo(final TodoRequest todoRequest, final String userId);

}
