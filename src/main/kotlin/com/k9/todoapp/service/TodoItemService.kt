package com.k9.todoapp.service

import com.k9.todoapp.model.TodoItemCollectionDto
import com.k9.todoapp.model.TodoItemDto
import com.k9.todoapp.repository.TodoItemRepository
import com.k9.todoapp.util.PageableUtil
import com.k9.todoapp.util.TodoItemUtil
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class TodoItemService(private val todoItemRepository: TodoItemRepository) {
    fun getAllTodoItems(
        optionalPage: Optional<Int> = Optional.of(0),
        optionalLimit: Optional<Int> = Optional.of(5),
        optionalSort: Optional<Sort.Direction> = Optional.of(Sort.Direction.ASC),
        optionalSortBy: Optional<String> = Optional.of("id")
    ): Optional<TodoItemCollectionDto> {
        val pageable = PageableUtil.getPageable(
            optionalPage,
            optionalLimit,
            optionalSort,
            Optional.of(todoItemRepository.count().toInt()),
            optionalSortBy
        )
        val results = todoItemRepository.findByDeletedDateIsNull(pageable).map(TodoItemUtil::convertToDto).content
        val page = PageImpl(results, pageable, todoItemRepository.count())
        return Optional.of(
            TodoItemCollectionDto(
                count = page.numberOfElements,
                totalPages = page.totalPages,
                page.content
            )
        )
    }

    fun getTodoItem(todoItemId: Long): Optional<TodoItemDto> {
        val optionalTodoItem =
            todoItemRepository.findByIdAndDeletedDateIsNull(todoItemId)
                .filter { todoItem -> todoItem.deletedDate == null }
        return if (optionalTodoItem.isPresent) Optional.of(TodoItemUtil.convertToDto(optionalTodoItem.get())) else Optional.empty()
    }

    fun addTodoItem(todoItemDto: TodoItemDto): Optional<TodoItemDto> {
        val todoItem = TodoItemUtil.convertToEntity(todoItemDto)
        todoItem.createdDate = Date()
        val savedTodoItem = todoItemRepository.save(todoItem)
        return Optional.of(TodoItemUtil.convertToDto(savedTodoItem))
    }

    fun updateTodoItem(todoItemId: Long, todoItemDto: TodoItemDto): Optional<TodoItemDto> {
        val optionalTodoItem = todoItemRepository.findByIdAndDeletedDateIsNull(todoItemId)
        return if (optionalTodoItem.isPresent) {
            val todoItem = optionalTodoItem.get()
            todoItem.taskName = todoItemDto.taskName
            todoItem.isFinished = todoItemDto.isFinished
            todoItem.updatedDate = Date()
            val savedTodoItem = todoItemRepository.save(todoItem)
            Optional.of(TodoItemUtil.convertToDto(savedTodoItem))
        } else {
            Optional.empty()
        }
    }

    fun deleteTodoItem(todoItemId: Long): Optional<TodoItemDto> {
        val optionalTodoItem = todoItemRepository.findByIdAndDeletedDateIsNull(todoItemId)
        return if (optionalTodoItem.isPresent) {
            val todoItem = optionalTodoItem.get()
            todoItem.deletedDate = Date()
            val savedTodoItem = todoItemRepository.save(todoItem)
            Optional.of(TodoItemUtil.convertToDto(savedTodoItem))
        } else {
            Optional.empty()
        }
    }
}