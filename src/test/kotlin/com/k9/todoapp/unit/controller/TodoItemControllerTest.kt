package com.k9.todoapp.unit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.k9.todoapp.model.TodoItem
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
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
    fun `get all empty todo items sort by asc`() {
        mockMvc.get("/todos?sort=asc").andExpect {
            content {
                contentType(MediaType.APPLICATION_JSON)
            }
            status {
                isOk()
            }
        }
    }

    @Test
    fun `get all empty todo items sort by desc`() {
        mockMvc.get("/todos?sort=desc").andExpect {
            content {
                contentType(MediaType.APPLICATION_JSON)
            }
            status {
                isOk()
            }
        }
    }

    @Test
    fun `add invalid todo item and get it`() {
        val todoItem = TodoItem(id = 1, taskName = "")
        val performPost = mockMvc.post("/todos") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPost.andDo { print() }.andExpect {
            status {
                isBadRequest()
            }
        }

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            status {
                isNotFound()
            }
        }
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `add todo item and get it`() {
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

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            content {
                contentType(MediaType.APPLICATION_JSON)
            }
            status {
                isOk()
            }
            jsonPath("$.id") { value(todoItem.id) }
            jsonPath("$.taskName") { value(todoItem.taskName) }
        }
    }


    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
                value(todoItem.id)
            }
            jsonPath("$.taskName") {
                value(todoItem.taskName)
            }
        }
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update unknown todo and put then check it`() {
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
        todoItem.id = 2
        todoItem.taskName = "Update Task 2"

        val performPut = mockMvc.put("/todos/${todoItem.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPut.andDo { print() }.andExpect {
            status {
                isNotFound()
            }
        }

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            status { isNotFound() }
        }
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

        val performPatch = mockMvc.patch("/todos/${todoItem.id}") {
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
                value(todoItem.id)
            }
            jsonPath("$.taskName") {
                value(todoItem.taskName)
            }
        }
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `update todo and patch then check it`() {
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
        todoItem.id = 2
        todoItem.taskName = "Update Task 2"

        val performPatch = mockMvc.patch("/todos/${todoItem.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(todoItem)
        }
        performPatch.andDo { print() }.andExpect {
            status {
                isNotFound()
            }
        }

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            status { isNotFound() }
        }
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    fun `add todo and delete unknown todo then check it`() {
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
        todoItem.id = 2
        val performDelete = mockMvc.delete("/todos/${todoItem.id}")
        performDelete.andDo { print() }.andExpect {
            status {
                isNotFound()
            }
        }

        mockMvc.get("/todos/${todoItem.id}").andDo { print() }.andExpect {
            status { isNotFound() }
        }
    }

}