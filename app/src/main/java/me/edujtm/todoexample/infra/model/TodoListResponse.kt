package me.edujtm.todoexample.infra.model

import me.edujtm.todoexample.domain.model.Todo

data class TodoListResponse(
    val todos: List<Todo>
)