package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.services.department.DepartmentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/departments")
class DepartmentController(
    val departmentService: DepartmentService
) {

    @GetMapping
    fun getDepartments(): List<DepartmentDto> {
        return departmentService.getDepartments().map { it.toTransferObject() }
    }
}
