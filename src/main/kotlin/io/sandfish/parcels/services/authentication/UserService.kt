package io.sandfish.parcels.services.authentication

import io.sandfish.parcels.domain.User
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.dtos.UserDto
import io.sandfish.parcels.dtos.UserWithRolesDto
import io.sandfish.parcels.repositories.RoleRepository
import io.sandfish.parcels.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val roleService: RoleService,
    private val bcryptEncoder: BCryptPasswordEncoder
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByName(username)
        return org.springframework.security.core.userdetails.User(
            user.name,
            user.password,
            getAuthority(user)
        )
    }

    private fun getAuthority(user: User): Set<SimpleGrantedAuthority> {
        val authorities: MutableSet<SimpleGrantedAuthority> = HashSet()
        user.roles.forEach { role ->
            authorities += SimpleGrantedAuthority("ROLE_" + role.name)
        }
        return authorities
    }

    fun findByName(name: String): User {
        return userRepository.findByName(name)
    }

    fun save(user: UserDto): User {
        val userRole = roleService.findByName("USER")

        val nUser = User(
            null,
            name = user.username,
            password = bcryptEncoder.encode(user.password),
            roles = mutableSetOf(userRole)
        )

        return userRepository.save(nUser)
    }

    /**
     * Add a new role to an existing user.
     * Needs to get new JWT token after to take effect.
     */
    fun addRoleToUser(id: Long, role: RoleDto): User {
        val user = this.userRepository.findById(id).orElseThrow { RuntimeException() }
        val newRole = this.roleRepository.findByName(role.name)

        user.roles.add(newRole)

        return this.userRepository.save(user)
    }

    fun getUsers(): List<User> {
        return this.userRepository.findAll().toList()
    }
}
