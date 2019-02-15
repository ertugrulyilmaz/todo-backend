package io.todo.todo.repository;

import io.todo.todo.domain.entity.User;
import io.todo.todo.repository.impl.UserRepositoryImpl;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRepositoryTest extends TestBase {

	private static final String INSERT_USER = "INSERT INTO users (id, email, first_name, last_name, password, salt, created_at) VALUES (:id, :email, :firstName, :lastName, :password, :salt, :createdAt)";
	private static final String GET_USER_BY_EMAIL = " SELECT * FROM users WHERE email = :email ";
	private static final String GET_USER_BY_ID = " SELECT * FROM users WHERE id = :userId ";
	private static final String GET_USER_BY_USER_ID_AND_TOKEN = " SELECT * FROM users WHERE id = :id AND token = :token ";
	private static final String DELETE_TOKEN_BY_EMAIl = " UPDATE users SET token = null WHERE email = :email ";
	private static final String UPDATE_TOKEN = " UPDATE users SET token = :token WHERE id = :id ";

	private UserRepositoryImpl userRepository;

	@Test
	public void testSave() {
		userRepository = new UserRepositoryImpl(sql2o, executor);

		final String userId = "userId";
		final String email="first@last.com";
		final String firstName = "firstname";
		final String lastName = "lastname";
		final String password = "password";
		final String salt = "salt";
		final long createdAt = new Date().getTime();
		final User user = new User(userId, email, firstName, lastName, password, salt, createdAt);

		when(sql2o.open()).thenReturn(connection);
		when(connection.createQuery(INSERT_USER)).thenReturn(query);
		when(query.setAutoDeriveColumnNames(true)).thenReturn(query);
		when(query.bind(user)).thenReturn(query);
		when(query.executeUpdate()).thenReturn(connection);

		final Boolean isSaved = userRepository.save(user).join();

		verify(sql2o).open();
		verify(connection).createQuery(INSERT_USER);
		verify(query).setAutoDeriveColumnNames(true);
		verify(query).bind(user);
		verify(query).executeUpdate();

		assertTrue(isSaved);
	}

}
