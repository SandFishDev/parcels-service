package io.sandfish.parcels.repositories

import io.sandfish.parcels.domain.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, Long>{
    fun findByName(username: String): Optional<User>
}
