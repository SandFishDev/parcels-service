package io.sandfish.parcels.services.authentication

import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.repositories.RoleRepository
import org.springframework.stereotype.Service


/**
 * Service for retrieving and adding new roles.
 */
@Service
class RoleService(private val roles: RoleRepository) {

    /**
     * Get all roles.
     */
    fun getRoles(): List<Role> {
        return roles.findAll().toList()
    }

    /**
     * get a role by its name.
     */
    fun findByName(name: String): Role {
        return roles.findByName(name)
    }

    /**
     * create a new role with no users associated to it.
     */
    fun createRole(role: Role): Role {
        return roles.save(role)
    }
}
