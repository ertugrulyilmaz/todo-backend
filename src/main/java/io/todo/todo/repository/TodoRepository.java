package io.todo.todo.repository;

import io.todo.todo.domain.entity.Todo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TodoRepository {

	CompletableFuture<List<Todo>> findAll(final String userId);

	CompletableFuture<Optional<Todo>> save(final Todo todo);

	CompletableFuture<Optional<Todo>> update(final Todo todo);

}
