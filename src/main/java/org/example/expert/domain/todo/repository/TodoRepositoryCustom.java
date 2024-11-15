package org.example.expert.domain.todo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.example.expert.domain.todo.dto.response.TodoSummaryResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {
	//QueryDSL 을 위한 커스텀 DB 조회 메서드들을 선언 해둔 인터페이스
	Optional<Todo> findByIdWithUser(Long todoId);

	Page<TodoSummaryResponse> findTodosByKeywords(String titleKeyword, String nicknameKeyword, LocalDateTime startDate, LocalDateTime endTime, Pageable pageable);
}
