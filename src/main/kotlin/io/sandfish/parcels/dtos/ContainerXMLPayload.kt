package io.sandfish.parcels.dtos


import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.sandfish.parcels.domain.Address
import io.sandfish.parcels.domain.Receipient
import java.time.ZonedDateTime
import javax.xml.bind.annotation.XmlRootElement

/**
 * Data classes for deserializing the container XML payload.
 * Must not be used anywhere else.
 */
@XmlRootElement
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class ContainerXMLPayload(
    val id: Long,
    val shippingDate: ZonedDateTime,
    @JsonProperty("parcels") val parcels: List<ContainerParcel>
)

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class ContainerParcel(val value: Double, val weight: Double, val receipient: ParcelReceipient)

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class ParcelReceipient(val name: String, val address: ReceipientAddress) {
    fun toDomain(): Receipient {
        return Receipient(
            name = this.name,
            address = this.address.toDomain()
        )
    }
}

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class ReceipientAddress(val street: String, val houseNumber: String, val postalCode: String, val city: String) {
    fun toDomain(): Address {
        return Address(
            street = this.street,
            houseNumber = this.houseNumber,
            postalCode = this.postalCode,
            city = this.city
        )
    }
}
