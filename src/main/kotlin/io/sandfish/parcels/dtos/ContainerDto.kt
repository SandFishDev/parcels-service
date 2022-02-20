package io.sandfish.parcels.dtos

import io.sandfish.parcels.domain.ParcelState
import io.sandfish.parcels.services.department.DepartmentType
import java.time.ZonedDateTime

data class ContainerDto(
    val id: Long,
    val containerId: Long,
    val shippingDate: ZonedDateTime,
    val arrivalDate: ZonedDateTime,
    val parcels: List<ParcelDto>
)

data class ParcelDto(
    val id: Long,
    val value: Double,
    val weight: Double,
    val receipient: ReceipientDto,
    val department: DepartmentType,
    val state: ParcelState,
)

data class ReceipientDto(
    val name: String,
    val address: AddressDto
)

data class AddressDto(
    val street: String,
    val houseNumber: String,
    val postalCode: String,
    val city: String
)
