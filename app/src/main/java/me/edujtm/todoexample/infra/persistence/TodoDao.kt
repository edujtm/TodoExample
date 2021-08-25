package me.edujtm.todoexample.infra.persistence

import androidx.room.*
import me.edujtm.todoexample.domain.model.Todo

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo")
    fun getAllTodos(): List<Todo>

    @Query("SELECT * FROM todo WHERE id = :id")
    fun getTodoById(id: Int): Todo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodos(vararg todos: Todo)

    @Delete
    fun delete(todo: Todo)
}