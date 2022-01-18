package com.k9.todoapp.service

import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.model.TodoItemCollectionDto
import com.k9.todoapp.model.TodoItemDto
import com.k9.todoapp.repository.TodoItemRepository
import com.k9.todoapp.util.PageableUtil
import com.k9.todoapp.util.TodoItemUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.*

@Service
class TodoItemService(private val todoItemRepository: TodoItemRepository) {
    val logger: Logger = LoggerFactory.getLogger(TodoItemService::class.java)

    fun getAllTodoItems(
        optionalPage: Optional<Int> = Optional.of(0),
        optionalLimit: Optional<Int> = Optional.of(5),
        optionalSort: Optional<Sort.Direction> = Optional.of(Sort.Direction.ASC),
        optionalSortBy: Optional<String> = Optional.of("id")
    ): TodoItemCollectionDto {
        val pageable = PageableUtil.getPageable(
            optionalPage,
            optionalLimit,
            optionalSort,
            Optional.of(todoItemRepository.count().toInt()),
            optionalSortBy
        )
        val results = todoItemRepository.findByDeletedDateIsNull(pageable).map(TodoItemUtil::convertToDto).content
        val page = PageImpl(results, pageable, todoItemRepository.count())
        return TodoItemCollectionDto(
            count = page.numberOfElements,
            totalPages = page.totalPages,
            page.content
        )
    }

    @Cacheable(value = ["TodoItemDto"], key = "#todoItemId", unless = "#result==null")
    fun getTodoItem(todoItemId: Long): Optional<TodoItem> {
        logger.info("Get todo id = $todoItemId")
        val optionalTodoItem =
            todoItemRepository.findByIdAndDeletedDateIsNull(todoItemId)
                .filter { todoItem -> todoItem.deletedDate == null }
        return if (optionalTodoItem.isPresent) Optional.of(optionalTodoItem.get()) else Optional.empty()
    }

    fun addTodoItem(todoItemDto: TodoItemDto): TodoItem {
        val todoItem = TodoItemUtil.convertToEntity(todoItemDto)
        todoItem.createdDate = Date()
        val savedTodoItem = todoItemRepository.save(todoItem)
        return savedTodoItem
    }

    @CachePut(value = ["TodoItemDto"], key = "#todoItemId")
    fun updateTodoItem(todoItemId: Long, todoItemDto: TodoItemDto): Optional<TodoItemDto> {
        logger.info("Update todo id = $todoItemId")
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

    @CacheEvict(value = ["TodoItemDto"], allEntries = true)
    fun deleteTodoItem(todoItemId: Long): Optional<TodoItem> {
        logger.info("Delete todo id = $todoItemId")
        val optionalTodoItem = todoItemRepository.findByIdAndDeletedDateIsNull(todoItemId)
        return if (optionalTodoItem.isPresent) {
            val todoItem = optionalTodoItem.get()
            todoItem.deletedDate = Date()
            val savedTodoItem = todoItemRepository.save(todoItem)
            Optional.of(savedTodoItem)
        } else {
            Optional.empty()
        }
    }
}