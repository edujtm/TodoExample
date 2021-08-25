package me.edujtm.todoexample.domain.utils

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val error: Throwable) : Result<Nothing>() {
        companion object {
            fun withMessage(message: String): Failure {
                val error = Throwable(message)
                return Failure(error);
            }
        }
    }
}
