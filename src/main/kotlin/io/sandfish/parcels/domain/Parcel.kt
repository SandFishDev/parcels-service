package io.sandfish.parcels.domain

import com.vladmihalcea.hibernate.type.json.JsonBlobType
import io.sandfish.parcels.dtos.ParcelDto
import io.sandfish.parcels.services.department.DepartmentType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.*

@Entity
@Table(name = "parcel")
@TypeDef(name = "json", typeClass = JsonBlobType::class)
class Parcel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false) val value: Double,

    @Column(nullable = false) val weight: Double,

    @Column(nullable = false) val department: DepartmentType,

    @Column(nullable = false) var state: ParcelState,

    @Type(type = "json")
    @Column(nullable = false, columnDefinition = "json")
    val metadata: Receipient,

    @ManyToOne
    @JoinColumn(name = "container_id")
    val container: Container?,
) {

    constructor(
        value: Double,
        weight: Double,
        metadata: Receipient,
        department: DepartmentType,
        state: ParcelState
    ) : this(
        null, value, weight, department, state, metadata, null
    )


    fun toTransferObject(): ParcelDto {
        return ParcelDto(
            id = this.id!!,
            value = this.value,
            weight = this.weight,
            receipient = this.metadata.toTransferObject(),
            department = this.department,
            state = this.state
        )
    }
}
