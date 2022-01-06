package com.k9.todoapp.repository;

import com.k9.todoapp.model.TodoItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoItemRepository : JpaRepository<TodoItem, Long> {
}