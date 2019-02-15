package io.todo.todo.controller;

import io.todo.todo.domain.dto.request.RegistrationRequest;
import io.todo.todo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value="/v1/auth", produces = APPLICATION_JSON_VALUE)
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public CompletableFuture<ResponseEntity<Boolean>> register(@RequestBody final RegistrationRequest registration) {
		return userService
				.register(registration)
				.thenApply(ResponseEntity::ok);
	}

	@PostMapping("/logout")
	public CompletableFuture<ResponseEntity<Boolean>> logout(final Principal principal) {
		return userService
				.deleteToken(principal.getName())
				.thenApply(ResponseEntity::ok);
	}

	@RequestMapping(value = "/check", method = RequestMethod.HEAD)
	public void checkToken() {
	}

}
