package io.sandfish.parcels.domain

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

