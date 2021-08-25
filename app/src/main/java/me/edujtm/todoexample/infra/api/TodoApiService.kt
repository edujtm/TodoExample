package me.edujtm.todoexample.infra.api

import me.edujtm.todoexample.domain.model.Todo
import me.edujtm.todoexample.infra.model.TodoApiResponse
import me.edujtm.todoexample.infra.model.TodoListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApiService {

    @GET("/api/todos")
    suspend fun getAllTodos() : TodoListResponse

    @PUT("/api/todos/{id}")
    suspend fun updateTodo(@Path("id") todoId: Int, @Body todo: Todo): TodoApiResponse
}