package com.k9.todoapp.util

import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.model.TodoItemDto
import org.modelmapper.ModelMapper

object TodoItemUtil {
    private val modelMapper: ModelMapper = ModelMapper()

    fun convertToDto(todoItem: TodoItem): TodoItemDto = modelMapper.map(todoItem, TodoItemDto::class.java)
    fun convertToEntity(todoItemDto: TodoItemDto): TodoItem = modelMapper.map(todoItemDto, TodoItem::class.java)
}