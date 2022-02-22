package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.services.authentication.RoleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/roles")
class RoleController(
    val roleService: RoleService
) {

    @GetMapping
    fun getRoles(): List<RoleDto> {
        return roleService.getRoles().map { it.toTransferObject() }
    }
}
