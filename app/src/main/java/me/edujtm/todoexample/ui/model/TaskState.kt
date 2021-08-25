package me.edujtm.todoexample.ui.model

import me.edujtm.todoexample.domain.utils.Result

sealed class TaskState<out T> {
    object Loading : TaskState<Nothing>()
    data class Success<T>(val data: T) : TaskState<T>()
    data class Failure(val error: Throwable) : TaskState<Nothing>() {
        companion object {
            fun withMessage(message: String): Failure {
                val error = Throwable(message)
                return Failure(error);
            }
        }
    }

    companion object {
        fun <T> fromResult(result: Result<T>): TaskState<T> {
            return when (result) {
                is Result.Success -> Success(result.data)
                is Result.Failure -> Failure(result.error)
            }
        }
    }
}
