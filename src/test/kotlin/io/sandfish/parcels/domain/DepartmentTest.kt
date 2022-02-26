package io.sandfish.parcels.domain

import org.assertj.core.api.AssertionsForClassTypes
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DepartmentTest {

    @Test
    fun toTransferObject() {
        val department = Department(
            1, "Mail", 1, mutableSetOf(
                Department(2, "Test", 2, mutableSetOf(), mutableSetOf(), mutableSetOf())
            ),
            mutableSetOf(),
            mutableSetOf()
        ).toTransferObject()

        assertThat(department.id).isEqualTo(1L)
        assertThat(department.name).isEqualTo("Mail")
        assertThat(department.priority).isEqualTo(1)
        assertThat(department.successors.size).isEqualTo(1)
    }
}
