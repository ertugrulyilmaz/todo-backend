package io.todo.todo.service.impl;

import io.todo.todo.domain.dto.request.RegistrationRequest;
import io.todo.todo.domain.entity.User;
import io.todo.todo.repository.UserRepository;
import io.todo.todo.service.UserService;
import io.todo.todo.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CryptoUtil cryptoUtil;

	public UserServiceImpl(final UserRepository userRepository, final CryptoUtil cryptoUtil) {
		this.userRepository = userRepository;
		this.cryptoUtil = cryptoUtil;
	}

	@Override
	public CompletableFuture<Boolean> register(RegistrationRequest registrationRequest) {
		final String email = registrationRequest.getEmail();
		final Optional<User> maybeUser = userRepository.getUserByEmail(email).join();

		if (maybeUser.isPresent()) {
			return CompletableFuture.supplyAsync(() -> false);
		} else {
			final String firstName = registrationRequest.getFirstName();
			final String lastName = registrationRequest.getLastName();
			final String password = registrationRequest.getPassword();
			final String userId = UUID.randomUUID().toString();
			final String salt = cryptoUtil.getSalt();
			final String encodedPassword = cryptoUtil.encodePassword(password, salt);
			final long date = new Date().getTime();
			final User user = new User(userId, email, firstName, lastName, encodedPassword, salt, date);

			return userRepository.save(user);
		}
	}

	@Override
	public CompletableFuture<Boolean> deleteToken(final String email) {
		return userRepository.deleteToken(email);
	}

	@Override
	public CompletableFuture<Optional<User>> getUserByEmail(final String email) {
		return userRepository.getUserByEmail(email);
	}

	@Override
	public CompletableFuture<Optional<User>> getUserById(final String userId) {
		return userRepository.getUserById(userId);
	}

	@Override
	public CompletableFuture<Optional<User>> findTokenBy(final String userId, final String token) {
		return userRepository.findTokenBy(userId, token);
	}

	@Override
	public CompletableFuture<Boolean> saveToken(String userId, String token) {
		return userRepository.saveToken(userId, token);
	}

}