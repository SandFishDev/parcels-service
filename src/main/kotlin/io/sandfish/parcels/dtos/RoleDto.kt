package io.sandfish.parcels.dtos

import io.sandfish.parcels.domain.Role

data class RoleDto(
    val id: Long?,
    val name: String,
    val description: String?
) {
    constructor(name: String) : this(null, name, null)
    constructor(name: String, description: String) : this(null, name, description)

    fun toDomain(): Role {
        return Role(
            id = id!!,
            name = name,
            description = description,
            users = mutableSetOf()
        )
    }

}
