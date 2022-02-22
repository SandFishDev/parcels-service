package io.sandfish.parcels.dtos

import io.sandfish.parcels.domain.User
import net.minidev.json.annotate.JsonIgnore

data class UserDto(
    val username: String,
    @JsonIgnore val password: String
)

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

