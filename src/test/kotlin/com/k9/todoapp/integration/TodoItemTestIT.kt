package com.k9.todoapp.integration

import com.k9.todoapp.model.TodoItemCollectionDto
import com.k9.todoapp.model.TodoItemDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TodoItemTestIT @Autowired constructor(private val webTestClient: WebTestClient) {
    @Order(1)
    @Test
    fun `add item`() {
        webTestClient.post().uri("/todos").body(BodyInserters.fromValue(TodoItemDto(taskName = "Task 1"))).exchange()
            .expectStatus().isCreated
    }

    @Order(2)
    @Test
    fun `get all item`() {
        val todoItemCollectionDto = webTestClient.get().uri("/todos").exchange()
            .expectStatus().isOk.expectBody(TodoItemCollectionDto::class.java).returnResult().responseBody
        assertThat(todoItemCollectionDto).hasFieldOrPropertyWithValue("count", 1)
            .hasFieldOrPropertyWithValue("totalPages", 1)
        assertThat(todoItemCollectionDto?.results).hasSize(1)
        val todoItemDto = todoItemCollectionDto?.results?.get(0)
        assertThat(todoItemDto).hasFieldOrPropertyWithValue("taskName", "Task 1")
    }

    @Order(3)
    @Test
    fun `update item then get item`() {

        webTestClient.put().uri("/todos/1").body(BodyInserters.fromValue(TodoItemDto(taskName = "Update Task 1")))
            .exchange().expectStatus().isNoContent

        val todoItemDto = webTestClient.get().uri("/todos/1").exchange()
            .expectStatus().isOk.expectBody(TodoItemDto::class.java).returnResult().responseBody
        assertThat(todoItemDto).hasFieldOrPropertyWithValue("taskName", "Update Task 1")
    }

    @Order(4)
    @Test
    fun `delete item then get item not found`() {
        webTestClient.delete().uri("/todos/1")
            .exchange().expectStatus().isOk

        webTestClient.get().uri("/todos/1").exchange()
            .expectStatus().isNotFound
    }
}