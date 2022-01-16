package com.k9.todoapp

import com.k9.todoapp.controller.TodoItemController
import com.k9.todoapp.repository.TodoItemRepository
import com.k9.todoapp.service.TodoItemService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TodoAppApplicationTests {

    @Autowired
    private lateinit var todoItemController: TodoItemController

    @Autowired
    private lateinit var todoItemService: TodoItemService

    @Autowired
    private lateinit var todoItemRepository: TodoItemRepository

    @Test
    fun contextLoads() {
        assertThat(todoItemController).isNotNull
        assertThat(todoItemService).isNotNull
        assertThat(todoItemRepository).isNotNull
    }

}
