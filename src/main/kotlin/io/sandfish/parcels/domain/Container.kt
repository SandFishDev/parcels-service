package io.sandfish.parcels.domain

import io.sandfish.parcels.dtos.ContainerDto
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
@Table(name = "container")
class Container(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(name = "container_id", nullable = false) val containerId: Long,
    @Column(name = "shipping_date", nullable = false) val shippingDate: ZonedDateTime,
    @Column(name = "arrival_date", nullable = false) val arrivalDate: ZonedDateTime = ZonedDateTime.now(),

    @OneToMany(mappedBy = "container", cascade = [CascadeType.PERSIST])
    val parcels: List<Parcel>
) {

    constructor(
        containerId: Long,
        shippingDate: ZonedDateTime,
        arrivalDate: ZonedDateTime,
        parcels: List<Parcel>
    ) : this(null, containerId, shippingDate, arrivalDate, parcels)


    fun toTransferObject(): ContainerDto {
        return ContainerDto(
            id = this.id!!,
            containerId = this.containerId,
            shippingDate = this.shippingDate,
            arrivalDate = this.arrivalDate,
            parcels = this.parcels.map {
                it.toTransferObject()
            }
        )
    }
}
