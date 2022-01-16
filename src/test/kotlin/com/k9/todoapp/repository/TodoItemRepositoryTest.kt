package com.k9.todoapp.repository

import com.k9.todoapp.model.TodoItem
import com.k9.todoapp.util.PageableUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.util.*

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TodoItemRepositoryTest {
    @Autowired
    private lateinit var todoItemRepository: TodoItemRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager


    @BeforeEach
    fun initTodoItemData() {
        val deletedTodoItem = TodoItem(taskName = "Task 1", deletedDate = Date())
        testEntityManager.persist(deletedTodoItem)
        val newTodoItem = TodoItem(taskName = "Task 2", createdDate = Date())
        testEntityManager.persist(newTodoItem)
        testEntityManager.flush()
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
        assertThat(todoItem.createdDate).isInstanceOf(Date::class.java)
        assertThat(todoItem.deletedDate).isNull()
    }

    @Test
    fun `get todoItem by id that not deleted`() {
        val optionalTodoItem = todoItemRepository.findByIdAndDeletedDateIsNull(4L)
        assertThat(optionalTodoItem).isNotEmpty
        val todoItem = optionalTodoItem.get()
        assertThat(todoItem.createdDate).isNotNull
        assertThat(todoItem.createdDate).isInstanceOf(Date::class.java)
        assertThat(todoItem.deletedDate).isNull()
    }
}