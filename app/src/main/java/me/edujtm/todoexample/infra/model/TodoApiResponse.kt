package me.edujtm.todoexample.infra.model

import me.edujtm.todoexample.domain.model.Todo

data class TodoApiResponse(
    val message: String,
    val todo: Todo,
    val todos: List<Todo>
)