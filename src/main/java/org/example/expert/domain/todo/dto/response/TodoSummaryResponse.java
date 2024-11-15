package org.example.expert.domain.todo.dto.response;

import lombok.Getter;

@Getter
public class TodoSummaryResponse {
	private String title;
	private Integer managerCount;
	private Integer commentCount;

	public TodoSummaryResponse(String title, Integer managerCount, Integer commentCount) {
		this.title = title;
		this.managerCount = managerCount;
		this.commentCount = commentCount;
	}
}
