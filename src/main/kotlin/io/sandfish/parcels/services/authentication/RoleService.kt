package io.sandfish.parcels.services.authentication

import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.repositories.RoleRepository
import org.springframework.stereotype.Service


@Service
class RoleService(private val roles: RoleRepository) {

    fun getRoles(): List<Role> {
        return roles.findAll().toList()
    }

    fun findByName(name: String): Role {
        return roles.findByName(name)
    }

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
