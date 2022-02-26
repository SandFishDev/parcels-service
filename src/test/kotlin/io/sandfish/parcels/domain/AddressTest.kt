package io.sandfish.parcels.domain

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AddressTest {

    @Test
    fun toTransferObject() {
        val address = Address(
            "Java straat", "18", "3251BN", "Amersfoort"
        ).toTransferObject()

        assertThat(address.street).isEqualTo("Java straat")
        assertThat(address.houseNumber).isEqualTo("18")
        assertThat(address.postalCode).isEqualTo("3251BN")
        assertThat(address.city).isEqualTo("Amersfoort")
    }
}
