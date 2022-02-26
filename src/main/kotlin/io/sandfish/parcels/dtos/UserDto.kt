package io.sandfish.parcels.dtos

import net.minidev.json.annotate.JsonIgnore

data class UserDto(
    val username: String,
    @JsonIgnore val password: String
)

