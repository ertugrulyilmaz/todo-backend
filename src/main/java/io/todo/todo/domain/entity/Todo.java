package io.todo.todo.domain.entity;

import lombok.Data;

@Data
public class Todo {

	private String id;
	private String userId;
	private String item;
	private String status;
	private long createdAt;
	private long updatedAt;

}
