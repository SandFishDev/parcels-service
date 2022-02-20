package io.sandfish.parcels.domain

import io.sandfish.parcels.dtos.UserWithRolesDto
import javax.persistence.*


@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val password: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "USER_ROLES",
        joinColumns = [JoinColumn(name = "USER_ID")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID")],
    )
    val roles: MutableSet<Role>
) {
    fun toTransferObject(): UserWithRolesDto {
        return UserWithRolesDto(name, roles)
    }
}


