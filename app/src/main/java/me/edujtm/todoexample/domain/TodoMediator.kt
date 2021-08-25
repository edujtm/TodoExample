package me.edujtm.todoexample.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.edujtm.todoexample.domain.model.Todo
import me.edujtm.todoexample.infra.api.TodoApiService
import me.edujtm.todoexample.infra.persistence.TodoDao
import me.edujtm.todoexample.domain.utils.Result as Result


class TodoMediator(
    private val todoApi: TodoApiService,
    private val todoDao: TodoDao
) {

    suspend fun getTodos() : Result<List<Todo>> = withContext(Dispatchers.IO) {
        return@withContext try {
            val todosDb = todoDao.getAllTodos()
            if (todosDb.isNotEmpty()) {
                return@withContext Result.Success(todosDb)
            }

            val response = todoApi.getAllTodos()
            todoDao.insertTodos(todos = response.todos.toTypedArray())
            Result.Success(response.todos)
        } catch (e : Exception) {
            Result.Failure(e)
        }
    }

    suspend fun refreshTodos() = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = todoApi.getAllTodos()
            todoDao.insertTodos(todos = response.todos.toTypedArray())
            Result.Success(response.todos)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getTodoById(todoId: Int) = withContext(Dispatchers.IO) {
        return@withContext try {
            val todo = todoDao.getTodoById(todoId)
            Result.Success(todo)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun updateTodo(todo: Todo) = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = todoApi.updateTodo(todo.id, todo)
            todoDao.insertTodos(response.todo)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}