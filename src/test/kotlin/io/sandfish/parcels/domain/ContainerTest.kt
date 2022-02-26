package io.sandfish.parcels.domain

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime

internal class ContainerTest {

    @Test
    fun toTransferObject() {
        val shippingDate = ZonedDateTime.of(2022, 4, 4, 12, 40, 4, 0, ZoneId.systemDefault())
        val arrivalDate = ZonedDateTime.of(2022, 4, 5, 12, 40, 4, 0, ZoneId.systemDefault())
        val container = Container(
            1L, 22938L,
            shippingDate = shippingDate,
            arrivalDate = arrivalDate,
            emptyList()
        ).toTransferObject()

        assertThat(container.id).isEqualTo(1L)
        assertThat(container.containerId).isEqualTo(22938L)
        assertThat(container.shippingDate).isEqualTo(shippingDate)
        assertThat(container.arrivalDate).isEqualTo(arrivalDate)
    }
}
