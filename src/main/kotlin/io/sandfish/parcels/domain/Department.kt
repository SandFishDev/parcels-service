package io.sandfish.parcels.domain

import io.sandfish.parcels.dtos.DepartmentDto
import javax.persistence.*

/**
 * TODO Add fields to make them configurable
 */
@Entity
@Table(name = "department")
class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val name: String,
) {
    fun toTransferObject(): DepartmentDto {
        return DepartmentDto(id = this.id!!, name = this.name)
    }
}
