package io.todo.todo.service;

import io.todo.todo.domain.dto.request.RegistrationRequest;
import io.todo.todo.domain.entity.User;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {

	CompletableFuture<Boolean> register(final RegistrationRequest user);

	CompletableFuture<Boolean> deleteToken(final String email);

	CompletableFuture<Optional<User>> getUserByEmail(final String email);

	CompletableFuture<Optional<User>>  getUserById(final String userId);

	CompletableFuture<Optional<User>> findTokenBy(final String userId, final String token);

	CompletableFuture<Boolean> saveToken(final String userId, final String token);

}
