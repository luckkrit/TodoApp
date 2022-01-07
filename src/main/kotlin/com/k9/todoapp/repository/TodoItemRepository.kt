package com.k9.todoapp.repository;

import com.k9.todoapp.model.TodoItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TodoItemRepository : JpaRepository<TodoItem, Long> {
    fun findByDeletedDateIsNull(pageable: Pageable): Page<TodoItem>
    fun findByIdAndDeletedDateIsNull(todoItemId: Long): Optional<TodoItem>
}