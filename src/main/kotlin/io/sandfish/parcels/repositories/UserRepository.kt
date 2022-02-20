package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long>{
    fun findByName(username: String): User
}
