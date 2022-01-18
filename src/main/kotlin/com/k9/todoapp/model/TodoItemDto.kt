package com.k9.todoapp.model

import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TodoItemDto(
    var id: Long? = null,
    @field:NotNull(message = "Task name is not null")
    @field:NotBlank(message = "Task name is not empty")
    var taskName: String? = null,
    var isFinished: Boolean = false
) : Serializable
