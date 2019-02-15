package io.todo.todo.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegistrationRequest {

	@NotNull(message = "firstName can not be null")
	private String firstName;

	@NotNull(message = "lastName can not be null")
	private String lastName;

	@NotNull(message = "email can not be null")
	private String email;

	@NotNull(message = "password can not be null")
	private String password;

}
