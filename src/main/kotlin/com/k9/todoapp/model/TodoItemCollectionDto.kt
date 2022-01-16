package com.k9.todoapp.model

import java.io.Serializable

data class TodoItemCollectionDto(
    var count: Int,
    var totalPages: Int,
    var results: List<TodoItemDto>
) : Serializable