package com.k9.todoapp.unit.util

import com.k9.todoapp.util.PageableUtil
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.*

internal class PageableUtilTest {

    @Test
    fun `get default pageable when empty optional are used`() {
        val spyPageableUtil = spyk(PageableUtil)
        val pageable: Pageable = spyPageableUtil.getPageable(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        )
        verify(exactly = 1) {

            spyPageableUtil.getPageable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
            )
        }

        assertThat(pageable).isNotNull.hasFieldOrPropertyWithValue("page", 0).hasFieldOrPropertyWithValue("pageSize", 5)
        assertThat(pageable.sort).contains(Sort.Order.asc("id"))
    }

    @Test
    fun `get pageable offset of other than 0 and pageSize other than 0`() {

        val spyPageableUtil = spyk(PageableUtil)
        val pageable: Pageable = spyPageableUtil.getPageable(
            Optional.of(5),
            Optional.of(5),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        )
        verify(exactly = 1) {
            spyPageableUtil.getPageable(
                Optional.of(5),
                Optional.of(5),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
            )
        }

        assertThat(pageable).isNotNull.hasFieldOrPropertyWithValue("page", 5).hasFieldOrPropertyWithValue("pageSize", 5)
    }

    @Test
    fun `get pageable limit of 5 when pageSize is empty`() {

        val spyPageableUtil = spyk(PageableUtil)
        val pageable: Pageable = spyPageableUtil.getPageable(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of(5),
            Optional.empty()
        )
        verify(exactly = 1) {
            spyPageableUtil.getPageable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(5),
                Optional.empty()
            )
        }
        assertThat(pageable).isNotNull.hasFieldOrPropertyWithValue("pageSize", 5)
    }

    @Test
    fun `get pageable when direction and sortBy is not empty`() {
        val spyPageableUtil = spyk(PageableUtil)
        val pageable: Pageable = spyPageableUtil.getPageable(
            Optional.empty(),
            Optional.empty(),
            Sort.Direction.fromOptionalString("ASC"),
            Optional.empty(),
            Optional.of("id")
        )
        verify(exactly = 1) {
            spyPageableUtil.getPageable(
                Optional.empty(),
                Optional.empty(),
                Sort.Direction.fromOptionalString("ASC"),
                Optional.empty(),
                Optional.of("id")
            )
        }
        assertThat(pageable).isNotNull
        assertThat(pageable.sort).contains(Sort.Order.asc("id"))
    }
}