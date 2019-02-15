package io.todo.todo.service.impl;

import io.todo.todo.domain.dto.request.TodoRequest;
import io.todo.todo.domain.entity.Todo;
import io.todo.todo.repository.TodoRepository;
import io.todo.todo.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

	final TodoRepository todoRepository;

	public TodoServiceImpl(final TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Override
	public CompletableFuture<List<Todo>> getTodos(final String userId) {
		return todoRepository.findAll(userId);
	}

	@Override
	public CompletableFuture<Optional<Todo>> addTodo(final TodoRequest todoRequest, final String userId) {
		final Todo todo = new Todo();
		todo.setId(todoRequest.getId());
		todo.setUserId(userId);
		todo.setItem(todoRequest.getItem());
		todo.setStatus("PENDING");
		todo.setCreatedAt(new Date().getTime());

		return todoRepository.save(todo);
	}

	@Override
	public CompletableFuture<Todo> deleteTodo(final TodoRequest todoRequest, final String userId) {
		final Todo todo = new Todo();
		todo.setId(todoRequest.getId());
		todo.setUserId(userId);
		todo.setStatus("DELETED");
		todo.setUpdatedAt(new Date().getTime());

		return todoRepository.update(todo);
	}

	@Override
	public CompletableFuture<Todo> toggleTodo(final TodoRequest todoRequest, final String userId) {
		final Todo todo = new Todo();
		todo.setId(todoRequest.getId());
		todo.setUserId(userId);
		todo.setStatus(todoRequest.getStatus());
		todo.setUpdatedAt(new Date().getTime());

		return todoRepository.update(todo);
	}

}
