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

    @ManyToMany(mappedBy = "ancestors", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val successors: MutableSet<Department>,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "DEPARTMENT_GRAPH",
        joinColumns = [JoinColumn(name = "SUCCESSOR_ID")],
        inverseJoinColumns = [JoinColumn(name = "ANCESTOR_ID")]
    )
    val ancestors: MutableSet<Department>

) {
    fun toTransferObject(): DepartmentDto {
        return DepartmentDto(this.id!!, name = this.name, this.successors.map { it.id!! }.toSet())
    }
}
