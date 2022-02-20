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

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
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

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun saveUser(@RequestBody registeringUser: UserDto): User {
        return userService.save(UserDto(registeringUser.username, registeringUser.password))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = ["/{userId}/role"], method = [RequestMethod.POST])
    fun addRoleToUser(@PathVariable userId: Long, @RequestBody role: RoleDto): UserWithRolesDto {
        val user = userService.addRoleToUser(id = userId, role = role)
        return user.toTransferObject()
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    fun getUsers(): List<UserWithRolesDto> {
        return userService.getUsers().map { it.toTransferObject() }
    }
}
