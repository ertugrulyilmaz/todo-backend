package io.todo.todo.domain.dto.request;

import lombok.Data;

@Data
public class TodoRequest {

	private String id;
	private String item;
	private String status;

}
