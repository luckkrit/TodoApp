package com.k9.todoapp.service

import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.model.TodoItemDto
import com.k9.todoapp.repository.TodoItemRepository
import com.k9.todoapp.util.PageableUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
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
        every { todoItemRepository.findByDeletedDateIsNull(pageable).content } returns emptyList<TodoItem>()

        val optionalTodoCollectionDto = todoItemService.getAllTodoItems()

        verify(exactly = 1) { todoItemRepository.findByDeletedDateIsNull(pageable) }
        assertThat(optionalTodoCollectionDto).isNotEmpty
        val todoItemCollectionDto = optionalTodoCollectionDto.get()
        assertThat(todoItemCollectionDto).hasFieldOrPropertyWithValue("count", 0)
        assertThat(todoItemCollectionDto).hasFieldOrPropertyWithValue("totalPages", 0)
        val results = todoItemCollectionDto.results
        assertThat(results).hasSize(0)
    }

    @Test
    fun `get all 5 todo items`() {
        val pageable = PageableUtil.getPageable(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        )
        val todoItems = listOf(
            TodoItem(id = 1, taskName = "Task 1", createdDate = Date()),
            TodoItem(id = 2, taskName = "Task 2", createdDate = Date()),
            TodoItem(id = 3, taskName = "Task 3", createdDate = Date()),
            TodoItem(id = 4, taskName = "Task 4", createdDate = Date()),
            TodoItem(id = 5, taskName = "Task 5", createdDate = Date()),
        )
        every { todoItemRepository.findByDeletedDateIsNull(pageable) } returns PageImpl(todoItems)

        val optionalTodoCollectionDto = todoItemService.getAllTodoItems()
        verify(exactly = 1) { todoItemRepository.findByDeletedDateIsNull(pageable) }
        assertThat(optionalTodoCollectionDto).isNotEmpty
        val todoItemCollectionDto = optionalTodoCollectionDto.get()
        assertThat(todoItemCollectionDto).hasFieldOrPropertyWithValue("count", 5)
        assertThat(todoItemCollectionDto).hasFieldOrPropertyWithValue("totalPages", 1)
        val results = todoItemCollectionDto.results
        assertThat(results).hasSize(5)
    }

    @Test
    fun `get Todo by id`() {
        every { todoItemRepository.findByIdAndDeletedDateIsNull(1) } returns Optional.of(
            TodoItem(
                id = 1,
                taskName = "Task 1",
                isFinished = false
            )
        )

        val optionalTodoItemDto = todoItemService.getTodoItem(1)
        verify(exactly = 1) {
            todoItemRepository.findByIdAndDeletedDateIsNull(1)
        }
        assertThat(optionalTodoItemDto).isNotEmpty.get().hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("taskName", "Task 1").hasFieldOrPropertyWithValue("isFinished", false)
    }

    @Test
    fun `add Todo`() {
        val todoItem = TodoItem(id = 1L, taskName = "Task 1", createdDate = Date(), isFinished = false)
        val todoItemDto = TodoItemDto(taskName = "Task 1", isFinished = false)
        every { todoItemRepository.save(any()) } returns todoItem
        val optionalTodoItemDto = todoItemService.addTodoItem(todoItemDto)
        verify(exactly = 1) { todoItemRepository.save(any()) }
        assertThat(optionalTodoItemDto).isPresent.get().hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("taskName", "Task 1").hasFieldOrPropertyWithValue("isFinished", false)
    }

    @Test
    fun `update Todo`() {
        val todoItem = TodoItem(id = 1L, taskName = "Update Task 1", updatedDate = Date(), isFinished = false)
        val todoItemDto = TodoItemDto(taskName = "Update Task 1", isFinished = false)
        every { todoItemRepository.findByIdAndDeletedDateIsNull(1L) } returns Optional.of(todoItem)
        every { todoItemRepository.save(any()) } returns todoItem
        val optionalTodoItemDto = todoItemService.updateTodoItem(1L, todoItemDto)
        verify(exactly = 1) { todoItemRepository.findByIdAndDeletedDateIsNull(1L) }
        verify(exactly = 1) { todoItemRepository.save(any()) }
        assertThat(optionalTodoItemDto).isPresent.get().hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("taskName", "Update Task 1")
            .hasFieldOrPropertyWithValue("isFinished", false)
    }

    @Test
    fun `delete Todo`() {
        val todoItem = TodoItem(id = 1L, taskName = "Task 1", deletedDate = Date(), isFinished = false)
        every { todoItemRepository.findByIdAndDeletedDateIsNull(1L) } returns Optional.of(todoItem)
        every { todoItemRepository.save(any()) } returns todoItem
        val optionalTodoItemDto = todoItemService.deleteTodoItem(1L)
        verify(exactly = 1) { todoItemRepository.findByIdAndDeletedDateIsNull(1L) }
        verify(exactly = 1) { todoItemRepository.save(any()) }
        assertThat(optionalTodoItemDto).isPresent.get().hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("taskName", "Task 1").hasFieldOrPropertyWithValue("isFinished", false)
    }
}