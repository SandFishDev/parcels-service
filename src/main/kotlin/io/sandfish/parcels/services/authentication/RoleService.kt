package io.sandfish.parcels.services.authentication

import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.repositories.RoleRepository
import org.springframework.stereotype.Service


@Service
class RoleService(val roles: RoleRepository) {
    fun findByName(name: String): Role {
        return roles.findByName(name)
    }

    fun addRole(role: RoleDto): Role {
        return roles.save(
            Role(
                name = role.name,
                description = role.description
            )
        )
    }
}
