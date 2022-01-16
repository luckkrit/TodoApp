package com.k9.todoapp.controller

import com.k9.todoapp.model.TodoItemCollectionDto
import com.k9.todoapp.model.TodoItemDto
import com.k9.todoapp.service.TodoItemService
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
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

    @GetMapping("/{id}")
    fun getTodoItem(@PathVariable id: Long): ResponseEntity<TodoItemDto> {
        val optionalTodoItemDto = todoItemService.getTodoItem(id)
        return if (optionalTodoItemDto.isPresent) ResponseEntity.ok(optionalTodoItemDto.get()) else ResponseEntity.notFound()
            .build()
    }

    @PostMapping
    fun postTodoItem(@RequestBody todoItemDto: TodoItemDto): ResponseEntity<Any> {
        val optionalTodoItemDto = todoItemService.addTodoItem(todoItemDto)
        return if (optionalTodoItemDto.isPresent) ResponseEntity.created(URI.create("/todos/${optionalTodoItemDto.get().id}"))
            .build() else ResponseEntity.badRequest()
            .build()
    }

    @PutMapping("/{id}")
    fun putTodoItem(@PathVariable id: Long, @RequestBody todoItemDto: TodoItemDto): ResponseEntity<Any> {
        val optionalTodoItemDto = todoItemService.updateTodoItem(id, todoItemDto)
        return if (optionalTodoItemDto.isPresent) ResponseEntity.noContent().build() else ResponseEntity.notFound()
            .build()
    }

    @PatchMapping("/{id}")
    fun patchTodoItem(@PathVariable id: Long, @RequestBody todoItemDto: TodoItemDto): ResponseEntity<Any> {
        val optionalTodoItemDto = todoItemService.updateTodoItem(id, todoItemDto)
        return if (optionalTodoItemDto.isPresent) ResponseEntity.noContent().build() else ResponseEntity.notFound()
            .build()
    }

    @DeleteMapping("/{id}")
    fun deleteTodoItem(@PathVariable id: Long): ResponseEntity<Any> {
        val optionalTodoItemDto = todoItemService.deleteTodoItem(id)
        return if (optionalTodoItemDto.isPresent) ResponseEntity.ok().build() else ResponseEntity.notFound()
            .build()
    }
}