package com.k9.todoapp.service

import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.repository.TodoItemRepository
import com.k9.todoapp.util.PageableUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

internal class TodoItemServiceTest {

    private val todoItemRepository: TodoItemRepository = mockk(relaxed = true)

    private val todoItemService: TodoItemService = TodoItemService(todoItemRepository)

    @Test
    fun `get all empty todo`() {
        val pageable = PageableUtil.getPageable(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        )
        every { todoItemRepository.findAll(pageable).content } returns emptyList<TodoItem>()

        val todoItemDtos = todoItemService.getAllTodoItems()

        verify(exactly = 1) { todoItemRepository.findAll(pageable) }
        assertThat(todoItemDtos).isEmpty()
    }
}