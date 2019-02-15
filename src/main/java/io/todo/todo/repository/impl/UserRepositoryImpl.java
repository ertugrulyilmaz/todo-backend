package io.todo.todo.repository.impl;

import io.todo.todo.config.AsyncConfig;
import io.todo.todo.domain.entity.User;
import io.todo.todo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

	private static final String INSERT_USER = "INSERT INTO users (id, email, first_name, last_name, password, salt, created_at) VALUES (:id, :email, :firstName, :lastName, :password, :salt, :createdAt)";
	private static final String GET_USER_BY_EMAIL = " SELECT * FROM users WHERE email = :email ";
	private static final String GET_USER_BY_ID = " SELECT * FROM users WHERE id = :userId ";
	private static final String GET_USER_BY_USER_ID_AND_TOKEN = " SELECT * FROM users WHERE id = :id AND token = :token ";
	private static final String DELETE_TOKEN_BY_EMAIl = " UPDATE users SET token = null WHERE email = :email ";
	private static final String UPDATE_TOKEN = " UPDATE users SET token = :token WHERE id = :id ";

	private final Sql2o sql2o;
	private final Executor executor;

	public UserRepositoryImpl(final Sql2o sql2o, @Qualifier(AsyncConfig.TASK_EXECUTOR_REPOSITORY) final Executor executor) {
		this.sql2o = sql2o;
		this.executor = executor;
	}


	@Override
	public CompletableFuture<Boolean> save(final User user) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				con.createQuery(INSERT_USER)
						.bind(user)
						.setAutoDeriveColumnNames(true)
						.executeUpdate();

				return true;
			} catch (Exception e) {
				log.error("{}", e);

				return false;
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<User>> getUserByEmail(final String email) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				return Optional.ofNullable(con.createQuery(GET_USER_BY_EMAIL)
						.setAutoDeriveColumnNames(true)
						.addParameter("email", email)
						.executeAndFetchFirst(User.class));
			} catch (Exception e) {
				log.error("{}", e);

				return Optional.empty();
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<User>> getUserById(final String userId) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				return Optional.ofNullable(con.createQuery(GET_USER_BY_ID)
						.setAutoDeriveColumnNames(true)
						.addParameter("userId", userId)
						.executeAndFetchFirst(User.class));
			} catch (Exception e) {
				log.error("{}", e);

				return Optional.empty();
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Boolean> deleteToken(final String email) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				con.createQuery(DELETE_TOKEN_BY_EMAIl)
						.setAutoDeriveColumnNames(true)
						.addParameter("email", email)
						.executeUpdate();

				return true;
			} catch (Exception e) {
				log.error("{}", e);

				return false;
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Optional<User>> findTokenBy(String userId, String token) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				return Optional.ofNullable(con.createQuery(GET_USER_BY_USER_ID_AND_TOKEN)
						.setAutoDeriveColumnNames(true)
						.addParameter("id", userId)
						.addParameter("token", token)
						.executeAndFetchFirst(User.class));
			} catch (Exception e) {
				log.error("{}", e);

				return Optional.empty();
			}
		}, executor);
	}

	@Override
	public CompletableFuture<Boolean> saveToken(String userId, String token) {
		return CompletableFuture.supplyAsync(() -> {
			try (Connection con = sql2o.open()) {
				con.createQuery(UPDATE_TOKEN)
						.setAutoDeriveColumnNames(true)
						.addParameter("id", userId)
						.addParameter("token", token)
						.executeUpdate();

				return true;
			} catch (Exception e) {
				log.error("{}", e);

				return false;
			}
		}, executor);
	}

}