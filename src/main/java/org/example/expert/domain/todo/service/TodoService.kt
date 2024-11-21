package org.example.expert.domain.todo.service

import org.example.expert.client.WeatherClient
import org.example.expert.config.security.UserDetailsImpl
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TodoService(
    private val todoRepository: TodoRepository,
    private val weatherClient: WeatherClient
) {
    @Transactional
    fun saveTodo(loginUser: UserDetailsImpl, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val authUser = AuthUser(
            loginUser.id,
            loginUser.user.email,
            loginUser.user.userRole,
            loginUser.user.nickname
        )
        val user = User.fromAuthUser(authUser)
        val weather = weatherClient.todayWeather
        val newTodo = Todo(
            title = todoSaveRequest.title,
            contents = todoSaveRequest.contents,
            weather = weather,
            user = user
        )
        newTodo.initializeManagers()

        val savedTodo = todoRepository.save(newTodo)
        return TodoSaveResponse(
            savedTodo.id!!, //id 는 생성시 null 인 상태일 수 있기때문
            savedTodo.title,
            savedTodo.contents,
            weather,
            UserResponse(savedTodo.user.id!!, savedTodo.user.email, savedTodo.user.nickname)
        )
    }
}