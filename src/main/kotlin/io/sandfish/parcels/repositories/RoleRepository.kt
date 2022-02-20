package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.domain.User
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<Role, Long>{
    fun findByName(name: String): Role
}
