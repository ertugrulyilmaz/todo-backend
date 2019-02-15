package io.todo.todo.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Profile {

	private String email;
	private String firstName;
	private String lastName;

}
