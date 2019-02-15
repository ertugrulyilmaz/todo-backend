package io.todo.todo.repository.impl;

import io.todo.todo.config.AsyncConfig;
import io.todo.todo.domain.entity.Todo;
import io.todo.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Repository
public class TodoRepositoryImpl implements TodoRepository {

	private static final String FIND_ALL_BY_USER_ID = "SELECT id, user_id, item, status, created_at, updated_at FROM todos WHERE status != 'DELETED' AND user_id = :userId";
	private static final String INSERT_TODO = "INSERT INTO todos(id, user_id, item, status, created_at) " +
			" VALUES(:todoId, :userId, :item, :status, :createdAt)";
	private static final String UPDATE_TODO = "UPDATE todos SET status = :status, updated_at = :updatedAt WHERE id = :todoId AND user_id = :userId";

	private final Sql2o sql2o;
	private final Executor executor;

	public TodoRepositoryImpl(final Sql2o sql2o, @Qualifier(AsyncConfig.TASK_EXECUTOR_REPOSITORY) final Executor executor) {
		this.sql2o = sql2o;
		this.executor = executor;
	}

	@Override
	public CompletableFuture<List<Todo>> findAll(final String userId) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				return con.createQuery(FIND_ALL_BY_USER_ID)
						.setAutoDeriveColumnNames(true)
						.addParameter("userId", userId)
						.executeAndFetch(Todo.class);
			} catch (Exception e) {
				log.error("{}", e);

				return Collections.emptyList();
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<Todo>> save(final Todo todo) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				con.createQuery(INSERT_TODO)
						.addParameter("todoId", todo.getId())
						.addParameter("userId", todo.getUserId())
						.addParameter("item", todo.getItem())
						.addParameter("status", todo.getStatus())
						.addParameter("createdAt", todo.getCreatedAt())
						.executeUpdate();

				return Optional.of(todo);
			} catch (Exception e) {
				log.error("{}", e);

				return Optional.empty();
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<Todo>> update(final Todo todo) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				con.createQuery(UPDATE_TODO)
						.addParameter("todoId", todo.getId())
						.addParameter("userId", todo.getUserId())
						.addParameter("status", todo.getStatus())
						.addParameter("updatedAt", todo.getUpdatedAt())
						.executeUpdate();
				return Optional.of(todo);
			} catch (Exception e) {
				log.error("{}", e);

				return Optional.empty();
			}
		}, executor);
	}

}
