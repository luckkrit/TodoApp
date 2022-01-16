package com.k9.todoapp.unit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.model.TodoItemDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class TodoItemControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Test
    fun `get all empty todo items`() {
        mockMvc.get("/todos").andExpect {
            content {
                contentType(MediaType.APPLICATION_JSON)
            }
            status {
                isOk()
            }
        }
    }

    @Test
    fun `add todo item and get it`() {
        val todoItemDto = TodoItemDto(id = 1, taskName = "Task 1")
        val performPost = mockMvc.post("/todos") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItemDto)
        }
        performPost.andDo { print() }.andExpect {
            status {
                isCreated()
            }
        }

        mockMvc.get("/todos/${todoItemDto.id}").andDo { print() }.andExpect {
            content {
                contentType(MediaType.APPLICATION_JSON)
            }
            status {
                isOk()
            }
            jsonPath("$.id") { value(todoItemDto.id) }
            jsonPath("$.taskName") { value(todoItemDto.taskName) }
        }
    }

    @Test
    fun `add todo item`() {
        val todoItem = TodoItem(id = 1, taskName = "Task 1")
        val performPost = mockMvc.post("/todos") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPost.andDo { print() }.andExpect {
            status {
                isCreated()
            }
        }
    }

    @Test
    fun `add todo and put then check it`() {
        val todoItem = TodoItem(id = 1, taskName = "Task 1")
        val performPost = mockMvc.post("/todos") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPost.andDo { print() }.andExpect {
            status {
                isCreated()
            }
        }

        todoItem.taskName = "Update Task 1"

        val performPut = mockMvc.put("/todos/${todoItem.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPut.andDo { print() }.andExpect {
            status {
                isNoContent()
            }
        }

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            status { isOk() }
            jsonPath("$.id") {
                value("1")
            }
            jsonPath("$.taskName") {
                value("Update Task 1")
            }
        }
    }

    @Test
    fun `add todo and patch then check it`() {
        val todoItem = TodoItem(id = 1, taskName = "Task 1")
        val performPost = mockMvc.post("/todos") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPost.andDo { print() }.andExpect {
            status {
                isCreated()
            }
        }

        todoItem.taskName = "Update Task 1"

        val performPatch = mockMvc.put("/todos/${todoItem.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPatch.andDo { print() }.andExpect {
            status {
                isNoContent()
            }
        }

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            status { isOk() }
            jsonPath("$.id") {
                value("1")
            }
            jsonPath("$.taskName") {
                value("Update Task 1")
            }
        }
    }

    @Test
    fun `add todo and delete then check it`() {
        val todoItem = TodoItem(id = 1, taskName = "Task 1")
        val performPost = mockMvc.post("/todos") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPost.andDo { print() }.andExpect {
            status {
                isCreated()
            }
        }

        val performDelete = mockMvc.delete("/todos/${todoItem.id}")
        performDelete.andDo { print() }.andExpect {
            status {
                isOk()
            }
        }

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            status { isNotFound() }
        }
    }

}