package io.sandfish.parcels.domain

import io.sandfish.parcels.dtos.AddressDto
import io.sandfish.parcels.dtos.ReceipientDto


class Receipient(
    val name: String,
    val address: Address
) {
    fun toTransferObject(): ReceipientDto {
        return ReceipientDto(
            name = this.name,
            address = this.address.toTransferObject()
        )
    }
}

class Address(
    val street: String,
    val houseNumber: String,
    val postalCode: String,
    val city: String
) {
    fun toTransferObject(): AddressDto {
        return AddressDto(
            street = this.street,
            houseNumber = this.houseNumber,
            postalCode = this.postalCode,
            city = this.city,
        )
    }
}
