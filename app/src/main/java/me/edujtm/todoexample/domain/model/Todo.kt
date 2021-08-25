package me.edujtm.todoexample.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Todo(
    @PrimaryKey
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "status")
    val status: Boolean
)
