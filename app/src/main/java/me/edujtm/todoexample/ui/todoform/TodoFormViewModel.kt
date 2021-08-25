package me.edujtm.todoexample.ui.todoform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.edujtm.todoexample.domain.TodoMediator
import me.edujtm.todoexample.domain.model.Todo
import me.edujtm.todoexample.domain.utils.Result
import me.edujtm.todoexample.ui.model.TaskState

class TodoFormViewModel(
    private val todoMediator: TodoMediator
) : ViewModel() {

    private val _todoState = MutableStateFlow<TaskState<Todo>>(TaskState.Loading)
    val todoState : StateFlow<TaskState<Todo>> = _todoState.asStateFlow()

    fun getTodoById(todoId: Int) {
        _todoState.value = TaskState.Loading

        viewModelScope.launch {
            val result = todoMediator.getTodoById(todoId)
            _todoState.value = TaskState.fromResult(result)
        }
    }

    suspend fun updateTodo(todo: Todo) = todoMediator.updateTodo(todo)
}