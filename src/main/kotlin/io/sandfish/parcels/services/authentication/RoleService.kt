package io.sandfish.parcels.services.authentication

import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.repositories.RoleRepository
import org.springframework.stereotype.Service


/**
 * Service for retrieving and adding new roles
 */
@Service
class RoleService(private val roles: RoleRepository) {

    fun getRoles(): List<Role> {
        return roles.findAll().toList()
    }

    fun findByName(name: String): Role {
        return roles.findByName(name)
    }

    /**
     * Add a new role with no users associated to it.
     */
    fun addRole(role: RoleDto): Role {
        return roles.save(
            Role(
                name = role.name,
                description = role.description,
                users = mutableSetOf()
            ),
        )
    }
}
