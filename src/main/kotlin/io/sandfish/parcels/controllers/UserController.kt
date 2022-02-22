package io.sandfish.parcels.controllers

import io.sandfish.parcels.config.TokenProvider
import io.sandfish.parcels.domain.User
import io.sandfish.parcels.dtos.AuthToken
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.dtos.UserDto
import io.sandfish.parcels.dtos.UserWithRolesDto
import io.sandfish.parcels.services.authentication.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users")
class UserController(
    val authenticationManager: AuthenticationManager,
    val tokenProvider: TokenProvider,
    val userService: UserService
) {

    @PostMapping(value = ["/login"])
    fun generateToken(@RequestBody login: UserDto): ResponseEntity<AuthToken> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                login.username,
                login.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication
        val token: String = tokenProvider.generateToken(authentication)
        return ResponseEntity.ok(AuthToken(token))
    }

    @PostMapping(value = ["/register"])
    fun saveUser(@RequestBody registeringUser: UserDto): User {
        return userService.save(UserDto(registeringUser.username, registeringUser.password))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = ["/{userId}/role"])
    fun addRoleToUser(@PathVariable userId: Long, @RequestBody role: RoleDto): UserWithRolesDto {
        val user = userService.addRoleToUser(id = userId, role = role)
        return user.toTransferObject()
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = ["/{userId}"])
    fun updateUser(@PathVariable userId: Long, @RequestBody user: UserWithRolesDto): UserWithRolesDto {
        val updatedUser = userService.updateRoles(userId, user.toDomain())

        return updatedUser.toTransferObject()

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getUsers(): List<UserWithRolesDto> {
        return userService.getUsers().map { it.toTransferObject() }
    }
}
