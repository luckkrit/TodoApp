package com.k9.todoapp

import com.k9.todoapp.controller.TodoItemController
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TodoAppApplicationTests {

    @Autowired
    private lateinit var todoItemController: TodoItemController

    @Test
    fun contextLoads() {
        assertThat(todoItemController).isNotNull
    }

}
