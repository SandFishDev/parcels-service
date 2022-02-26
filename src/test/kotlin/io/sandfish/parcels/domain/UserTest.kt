package io.sandfish.parcels.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class UserTest {

    @Test
    fun toTransferObject() {
        val role1 = Role(1, "Test A", "Test A", mutableSetOf())
        val role2 = Role(2, "Test B", "Test B", mutableSetOf())

        val user = User(1, "Sander", "hunter22", roles = mutableSetOf(
            role1, role2
        )).toTransferObject()

        assertThat(user.id).isEqualTo(1)
        assertThat(user.username).isEqualTo("Sander")
        assertThat(user.roles).hasSize(2)

    }
}
