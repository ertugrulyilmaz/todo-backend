package io.todo.todo.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class User implements Serializable {

	private final String id;
	private final String email;
	private final String firstName;
	private final String lastName;
	private final String password;
	private final String salt;
	private final String token;
	private final long createdAt;

	public User(final String id, final String email, final String firstName, final String lastName, final String password, final String salt, final long createdAt) {
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.salt = salt;
		this.token = null;
		this.createdAt = createdAt;
	}

}
