package com.k9.todoapp.model

import java.io.Serializable

data class TodoItemDto(
    var id: Long? = null,
    var taskName: String? = null,
    var isFinished: Boolean? = null
) : Serializable
