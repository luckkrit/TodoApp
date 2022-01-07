package com.k9.todoapp.repository

import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.util.PageableUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TodoItemRepositoryTest {
    @Autowired
    private lateinit var todoItemRepository: TodoItemRepository

    @BeforeAll
    fun initTodoItemData() {
        val deletedTodoItem = TodoItem(taskName = "Task 1", deletedDate = Date())
        todoItemRepository.save(deletedTodoItem)
        val newTodoItem = TodoItem(taskName = "Task 2", createdDate = Date())
        todoItemRepository.save(newTodoItem)
    }

    @Test
    fun `get all todoItems that not deleted`() {
        val optionalTodoItem = todoItemRepository.findByDeletedDateIsNull(
            PageableUtil.getPageable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
            )
        ).content
        assertThat(optionalTodoItem).isNotEmpty.hasSize(1)
        val todoItem = optionalTodoItem[0]
        assertThat(todoItem).hasNoNullFieldsOrPropertiesExcept("deletedDate", "updatedDate")
        assertThat(todoItem.createdDate).isNotNull
        assertThat(todoItem.deletedDate).isNull()
    }

    @Test
    fun `get todoItem by id that not deleted`() {
        val optionalTodoItem = todoItemRepository.findByIdAndDeletedDateIsNull(2L)
        assertThat(optionalTodoItem).isNotEmpty
        val todoItem = optionalTodoItem.get()
        assertThat(todoItem.createdDate).isNotNull
        assertThat(todoItem.deletedDate).isNull()
    }
}