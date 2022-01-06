package com.k9.todoapp.service

import com.k9.todoapp.model.TodoItemDto
import com.k9.todoapp.repository.TodoItemRepository
import com.k9.todoapp.util.PageableUtil
import com.k9.todoapp.util.TodoItemUtil
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
    ): List<TodoItemDto> {
        val pageable = PageableUtil.getPageable(
            optionalPage,
            optionalLimit,
            optionalSort,
            Optional.of(todoItemRepository.count().toInt()),
            optionalSortBy
        )
        return todoItemRepository.findAll(pageable).map(TodoItemUtil::convertToDto).content
    }
}