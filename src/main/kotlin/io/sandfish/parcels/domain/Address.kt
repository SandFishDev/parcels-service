package io.sandfish.parcels.domain

import io.sandfish.parcels.dtos.AddressDto

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
