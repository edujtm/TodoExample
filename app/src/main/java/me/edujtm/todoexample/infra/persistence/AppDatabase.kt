package me.edujtm.todoexample.infra.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import me.edujtm.todoexample.domain.model.Todo

@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao() : TodoDao
}