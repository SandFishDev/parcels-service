package io.sandfish.parcels.dtos

import io.sandfish.parcels.domain.Role
import net.minidev.json.annotate.JsonIgnore

data class UserDto(
    val username: String,
    @JsonIgnore val password: String)

data class UserWithRolesDto(
    val username: String,
    val roles: MutableSet<Role>,
)
