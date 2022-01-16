package com.k9.todoapp.unit.util

import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.model.TodoItemDto
import com.k9.todoapp.util.TodoItemUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper

internal class TodoItemUtilTest {


    @Test
    fun `convert to TodoItemDto and then correct`() {
        val modelMapper = ModelMapper()
        val todoItem = TodoItem(1L, "Task 1", false)
        val todoItemDto = modelMapper.map(todoItem, TodoItemDto::class.java)
        val todoItemDto2 = TodoItemUtil.convertToDto(todoItem)
        assertThat(todoItemDto).hasFieldOrPropertyWithValue("id", 1L).hasFieldOrPropertyWithValue("taskName", "Task 1")
            .hasFieldOrPropertyWithValue("isFinished", false)

        assertThat(todoItemDto2).hasFieldOrPropertyWithValue("id", 1L).hasFieldOrPropertyWithValue("taskName", "Task 1")
            .hasFieldOrPropertyWithValue("isFinished", false)
    }

    @Test
    fun `convert to TodoItem and then correct`() {
        val modelMapper = ModelMapper()
        val todoItemDto = TodoItemDto(1L, "Task 1", true)
        val todoItem = modelMapper.map(todoItemDto, TodoItem::class.java)
        val todoItem2 = TodoItemUtil.convertToEntity(todoItemDto)
        assertThat(todoItem).hasFieldOrPropertyWithValue("id", 1L).hasFieldOrPropertyWithValue("taskName", "Task 1")
            .hasFieldOrPropertyWithValue("isFinished", true)

        assertThat(todoItem2).hasFieldOrPropertyWithValue("id", 1L).hasFieldOrPropertyWithValue("taskName", "Task 1")
            .hasFieldOrPropertyWithValue("isFinished", true)
    }
}