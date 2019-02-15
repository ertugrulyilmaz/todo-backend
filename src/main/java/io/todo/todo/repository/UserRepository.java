package io.todo.todo.repository;

import io.todo.todo.domain.entity.User;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {

	CompletableFuture<Boolean> save(final User user);

	CompletableFuture<Optional<User>> getUserByEmail(final String email);

	CompletableFuture<Optional<User>> getUserById(final String userId);

	CompletableFuture<Boolean> deleteToken(final String email);

	CompletableFuture<Optional<User>> findTokenBy(final String userId, final String token);

	CompletableFuture<Boolean> saveToken(final String userId, final String token);

}
