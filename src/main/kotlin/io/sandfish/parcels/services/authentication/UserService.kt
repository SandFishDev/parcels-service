package io.sandfish.parcels.services.authentication

import io.sandfish.parcels.controllers.exceptions.NotFoundException
import io.sandfish.parcels.domain.User
import io.sandfish.parcels.dtos.UserDto
import io.sandfish.parcels.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val bcryptEncoder: BCryptPasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = this.getUserByName(username)
        return org.springframework.security.core.userdetails.User(
            user.name,
            user.password,
            getAuthority(user)
        )
    }

    private fun getAuthority(user: User): Set<SimpleGrantedAuthority> {
        return user.roles.map { SimpleGrantedAuthority("ROLE_" + it.name) }.toSet()
    }

    fun save(user: UserDto): User {
        val savedUser: User = userRepository.save(
            User(
                null,
                name = user.username,
                password = bcryptEncoder.encode(user.password),
                roles = mutableSetOf(roleService.findByName("USER"))
            )
        )

        return userRepository.save(savedUser)
    }

    fun getUsers(): List<User> {
        return this.userRepository.findAll().toList()
    }

    fun getUserById(userId: Long): User {
        return this.userRepository.findById(userId).orElseThrow { NotFoundException("User with id $userId not found.") }
    }

    fun getUserByName(name: String) : User {
        return this.userRepository.findByName(name).orElseThrow { NotFoundException("User with name '$name' not found.") }
    }

    fun updateRoles(userId: Long, user: User): User {
        val retrievedUser = getUserById(userId)
        user.roles.also { retrievedUser.roles = it }

        return this.userRepository.save(retrievedUser)
    }

    fun delete(userId: Long) {
        val retrievedUser = getUserById(userId)

        retrievedUser.roles.forEach { role ->
            role.users.removeIf { it.id!! == userId }
        }

        this.userRepository.deleteById(userId)
    }
}
