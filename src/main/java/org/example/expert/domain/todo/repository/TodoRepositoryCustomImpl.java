package org.example.expert.domain.todo.repository;

import static org.example.expert.domain.manager.entity.QManager.*;
import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.user.entity.QUser.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.expert.domain.todo.dto.response.TodoSummaryResponse;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {
	// 클래스에서 커스텀 인터페이스를 구현후 메서드를 활용하여 쿼리보내기(QueryDSL)
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Todo> findByIdWithUser(Long todoId) {
		QTodo todo = QTodo.todo;
		QUser user = QUser.user;

		Todo foundTodo = queryFactory
			.selectFrom(todo)
			.leftJoin(todo.user, user).fetchJoin()
			.where(todo.id.eq(todoId))
			.fetchOne();

		return Optional.ofNullable(foundTodo);
	}

	@Override
	public Page<TodoSummaryResponse> findTodosByKeywords(
		String titleKeyword,
		String nicknameKeyword,
		LocalDateTime startDate,
		LocalDateTime endDate,
		Pageable pageable
	) {
		JPQLQuery<TodoSummaryResponse> query = queryFactory
			.select(Projections.constructor(
				TodoSummaryResponse.class, //Projections.constructor() 메서드는 TodoSummaryResponse.class 에 속한 필드만 포함하는 생성자 생성
				todo.title,
				todo.managers.size().as("managerCount"),
				todo.comments.size().as("commentCount")
			))
			.from(todo)
			.leftJoin(todo.managers, manager)
			.leftJoin(manager.user, user)
			.where(
				titleContains(titleKeyword),
				managerNicknameContains(nicknameKeyword),
				createdDateBetween(startDate, endDate)
			)
			.groupBy(todo.id) //동일한 Todo 의 ID를 그룹화하여 중복제거
			.orderBy(todo.createdAt.desc());
		// 페이지네이션 적용 (offset, limit 가 그부분)
		long total = query.fetchCount(); //조건에 맞는 전체 Todo 의 갯수를 가져와 총페이지 계산
		List<TodoSummaryResponse> results = query
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch(); //결과 조회

		return new PageImpl<>(results, pageable, total); //PageImpl 로 감싸서 반환
	}

	private BooleanExpression titleContains(String keyword) {
		return keyword != null ? todo.title.contains(keyword) : null;
	}

	private BooleanExpression managerNicknameContains(String managerNickname) {
		return managerNickname != null ? user.nickname.contains(managerNickname) : null;
	}

	private BooleanExpression createdDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
		if (startDate != null && endDate != null) {
			return todo.createdAt.between(startDate, endDate);
		} else if (startDate != null) {
			return todo.createdAt.goe(startDate);
		} else if (endDate != null) {
			return todo.createdAt.loe(endDate);
		} else {
			return null;
		}
	}
}