package com.k9.todoapp.controller

import com.k9.todoapp.model.TodoItemCollectionDto
import com.k9.todoapp.service.TodoItemService
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/todos")
class TodoItemController(private val todoItemService: TodoItemService) {
    @GetMapping
    fun getAllTodoItems(
        @RequestParam("page") optionalPage: Optional<Int>,
        @RequestParam("limit") optionalLimit: Optional<Int>,
        @RequestParam("sort") optionalSort: Optional<String>,
        @RequestParam("sortby") optionalSortBy: Optional<String>
    ): ResponseEntity<TodoItemCollectionDto> {
        val sort =
            if (optionalSort.isPresent && optionalSort.get().uppercase() == "ASC") Optional.of(
                Sort.Direction.valueOf(
                    optionalSort.get().uppercase()
                )
            ) else Optional.of(Sort.Direction.ASC)
        val todoItemCollectionDto = todoItemService.getAllTodoItems(optionalPage, optionalLimit, sort, optionalSortBy)
        return if (todoItemCollectionDto.isPresent) ResponseEntity.ok(todoItemCollectionDto.get()) else ResponseEntity.notFound()
            .build()
    }

    @GetMapping("/test")
    fun test() {
        throw IllegalStateException("test2")
    }

}