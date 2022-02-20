package io.sandfish.parcels.dtos

data class RoleDto(
    val id: Long?,
    val name: String,
    val description: String?
) {
    constructor(name: String) : this(null, name, null)
    constructor(name: String, description: String) : this(null, name, description)
}
