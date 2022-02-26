package io.sandfish.parcels.domain

import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.RuleDto
import javax.persistence.*

/**
 *
 */
@Entity
@Table(name = "department")
class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val name: String,

    var priority: Long,

    @ManyToMany(mappedBy = "ancestors", fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var successors: MutableSet<Department>,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "DEPARTMENT_GRAPH",
        joinColumns = [JoinColumn(name = "SUCCESSOR_ID")],
        inverseJoinColumns = [JoinColumn(name = "ANCESTOR_ID")]
    )
    val ancestors: MutableSet<Department>,


    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval=true)
    var rules: MutableSet<Rule>

) {

    fun toTransferObject(): DepartmentDto {
        return DepartmentDto(
            this.id!!,
            name = this.name,
            priority = priority,
            successors = this.successors.map { it.id!! }.toSet(), //Infinite loop otherwise
            rules = this.rules.map { RuleDto(it.key, it.sign, it.value)}.toSet()
        )
    }
}
