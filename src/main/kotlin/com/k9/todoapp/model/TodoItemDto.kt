package com.k9.todoapp.model

import org.springframework.cache.annotation.Cacheable
import java.io.Serializable
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Cacheable
data class TodoItemDto(
    var id: Long? = null,
    @field:NotNull(message = "Task name is not null")
    @field:NotBlank(message = "Task name is not empty")
    var taskName: String? = null,
    var isFinished: Boolean = false
) : Serializable
