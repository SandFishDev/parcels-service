package io.sandfish.parcels.domain

import io.sandfish.parcels.dtos.RoleDto
import javax.persistence.*


@Entity
@Table(name = "role")
class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    @Column val name: String? = null,
    @Column val description: String? = null,

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    val users: MutableSet<User>,
) {

    fun toTransferObject(): RoleDto {
        return RoleDto(id = id, name!!, description = description!!)
    }
}
