package me.edujtm.todoexample.ui.todolist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.edujtm.todoexample.domain.TodoMediator
import me.edujtm.todoexample.domain.model.Todo
import me.edujtm.todoexample.ui.model.TaskState
import me.edujtm.todoexample.domain.utils.Result as Result

class TodoViewModel(
    private val todoMediator: TodoMediator
) : ViewModel() {

    private val _todos = MutableStateFlow<TaskState<List<Todo>>>(
        TaskState.Loading
    )

    val todos: StateFlow<TaskState<List<Todo>>> = _todos.asStateFlow()

    fun getTodos() {
        _todos.value = TaskState.Loading;

        viewModelScope.launch {
            val result = todoMediator.getTodos()
            if (result is Result.Failure) {
                Log.e(
                "TodoViewModel",
                "Error occurred on todo request: ${result.error.message}",
                    result.error
                )
            }
            _todos.value = TaskState.fromResult(result)
        }
    }

    fun refreshTodos() {
        _todos.value = TaskState.Loading

        viewModelScope.launch {
            val result = todoMediator.refreshTodos()
            if (result is Result.Failure) {
                Log.e(
                    "TodoViewModel",
                    "Error occurred on while refreshing todos: ${result.error.message}",
                    result.error
                )
            }
            _todos.value = TaskState.fromResult(result)
        }
    }
}