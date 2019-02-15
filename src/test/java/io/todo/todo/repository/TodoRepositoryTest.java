package io.todo.todo.repository;

import io.todo.todo.domain.entity.Todo;
import io.todo.todo.repository.impl.TodoRepositoryImpl;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoRepositoryTest extends TestBase {

	private static final String FIND_ALL_BY_USER_ID = "SELECT id, user_id, item, status, created_at, updated_at FROM todos WHERE status != 'DELETED' AND user_id = :userId";
	private static final String INSERT_TODO = "INSERT INTO todos(id, user_id, item, status, created_at) " +
			" VALUES(:todoId, :userId, :item, :status, :createdAt)";
	private static final String UPDATE_TODO = "UPDATE todos SET status = :status, updated_at = :updatedAt WHERE id = :todoId AND user_id = :userId";

	private TodoRepositoryImpl todoRepository;

	@Test
	public void testFindAll() {
		todoRepository = new TodoRepositoryImpl(sql2o, executor);

		final String userId = UUID.randomUUID().toString();
		final Todo todo1 = new Todo();
		todo1.setId(UUID.randomUUID().toString());
		todo1.setUserId(userId);
		todo1.setItem("item1");
		todo1.setStatus("PENDING");
		todo1.setCreatedAt(new Date().getTime());

		final Todo todo2 = new Todo();
		todo2.setId(UUID.randomUUID().toString());
		todo2.setUserId(userId);
		todo2.setItem("item2");
		todo2.setStatus("COMPLETED");
		todo2.setCreatedAt(new Date().getTime());

		List<Todo> actual = Lists.list(todo1, todo2);

		when(sql2o.open()).thenReturn(connection);
		when(connection.createQuery(FIND_ALL_BY_USER_ID)).thenReturn(query);
		when(query.setAutoDeriveColumnNames(true)).thenReturn(query);
		when(query.addParameter("userId", userId)).thenReturn(query);
		when(query.executeAndFetch(Todo.class)).thenReturn(actual);

		final List<Todo> todos = todoRepository.findAll(userId).join();

		verify(sql2o).open();
		verify(connection).createQuery(FIND_ALL_BY_USER_ID);
		verify(query).addParameter("userId", userId);
		verify(query).executeAndFetch(Todo.class);

		assertEquals(todos.size(), actual.size());
		assertEquals(todos.get(0).getId(), actual.get(0).getId());
		assertEquals(todos.get(1).getId(), actual.get(1).getId());
	}

	@Test
	public void testSave() {
		todoRepository = new TodoRepositoryImpl(sql2o, executor);

		final String todoId = UUID.randomUUID().toString();
		final String userId = UUID.randomUUID().toString();
		final String item = "New Todo";
		final String status = "PENDING";
		final long createdAt = new Date().getTime();
		final Todo todo = new Todo();
		todo.setId(todoId);
		todo.setUserId(userId);
		todo.setItem(item);
		todo.setStatus(status);
		todo.setCreatedAt(createdAt);

		when(sql2o.open()).thenReturn(connection);
		when(connection.createQuery(INSERT_TODO)).thenReturn(query);
		when(query.setAutoDeriveColumnNames(true)).thenReturn(query);
		when(query.addParameter("todoId", todoId)).thenReturn(query);
		when(query.addParameter("userId", userId)).thenReturn(query);
		when(query.addParameter("item", item)).thenReturn(query);
		when(query.addParameter("status", status)).thenReturn(query);
		when(query.addParameter("createdAt", createdAt)).thenReturn(query);
		when(query.executeUpdate()).thenReturn(connection);

		Optional<Todo> maybeTodo = todoRepository.save(todo).join();
		final Todo expected = maybeTodo.get();

		assertEquals(todo.getId(), expected.getId());
		verify(sql2o).open();
		verify(connection).createQuery(INSERT_TODO);
		verify(query).addParameter("todoId", todoId);
		verify(query).addParameter("userId", userId);
		verify(query).addParameter("item", item);
		verify(query).addParameter("status", status);
		verify(query).addParameter("createdAt", createdAt);
		verify(query).executeUpdate();
	}

	@Test
	public void testUpdate() {
		todoRepository = new TodoRepositoryImpl(sql2o, executor);

		final String todoId = UUID.randomUUID().toString();
		final String userId = UUID.randomUUID().toString();
		final String status = "COMPLETED";
		final long updatedAt = new Date().getTime();
		final Todo todo = new Todo();
		todo.setId(todoId);
		todo.setUserId(userId);
		todo.setStatus(status);
		todo.setUpdatedAt(updatedAt);

		when(sql2o.open()).thenReturn(connection);
		when(connection.createQuery(UPDATE_TODO)).thenReturn(query);
		when(query.setAutoDeriveColumnNames(true)).thenReturn(query);
		when(query.addParameter("todoId", todoId)).thenReturn(query);
		when(query.addParameter("userId", userId)).thenReturn(query);
		when(query.addParameter("status", status)).thenReturn(query);
		when(query.addParameter("updatedAt", updatedAt)).thenReturn(query);
		when(query.executeUpdate()).thenReturn(connection);

		Optional<Todo> maybeTodo = todoRepository.update(todo).join();

		final Todo expected = maybeTodo.get();

		assertEquals(todo.getId(), expected.getId());
		verify(sql2o).open();
		verify(connection).createQuery(UPDATE_TODO);
		verify(query).addParameter("todoId", todoId);
		verify(query).addParameter("userId", userId);
		verify(query).addParameter("status", status);
		verify(query).addParameter("updatedAt", updatedAt);
		verify(query).executeUpdate();
	}

}
