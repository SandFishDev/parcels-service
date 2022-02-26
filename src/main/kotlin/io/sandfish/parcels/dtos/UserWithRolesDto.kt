package io.sandfish.parcels.dtos

import io.sandfish.parcels.domain.User

data class UserWithRolesDto(
    val id: Long,
    val username: String,
    val roles: MutableSet<RoleDto>,
) {
    fun toDomain(): User {
        return User(
            id = id,
            name = username,
            roles = roles.map { it.toDomain() }.toMutableSet()
        )
    }
}
