package io.sandfish.parcels.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ParcelTest {

    @Test
    fun toTransferObject() {
        val parcel = Parcel(
            1, 10.0, 20.0, "Mail", ParcelState.InProcessing,
            Receipient("Sander", Address("A", "B", "C", "D")),
            null
        ).toTransferObject()

        assertThat(parcel.id).isEqualTo(1)
        assertThat(parcel.value).isEqualTo(10.0)
        assertThat(parcel.weight).isEqualTo(20.0)
        assertThat(parcel.department).isEqualTo("Mail")
        assertThat(parcel.state).isEqualTo(ParcelState.InProcessing)
        assertThat(parcel.receipient.name).isEqualTo("Sander")
        assertThat(parcel.receipient.address.street).isEqualTo("A")
        assertThat(parcel.receipient.address.houseNumber).isEqualTo("B")
        assertThat(parcel.receipient.address.postalCode).isEqualTo("C")
        assertThat(parcel.receipient.address.city).isEqualTo("D")
    }
}
