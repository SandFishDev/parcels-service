package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.services.department.DepartmentService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/departments")
@PreAuthorize("hasRole('ADMIN')")
class DepartmentController(
    val departmentService: DepartmentService
) {

    @GetMapping
    fun getDepartments(): List<DepartmentDto> {
        return departmentService.getDepartments().map { it.toTransferObject() }
    }

    @GetMapping("/{id}")
    fun getDepartmentById(@PathVariable id: Long): DepartmentDto {
        return departmentService.getDepartmentById(id).toTransferObject()
    }

    @PostMapping
    fun createDepartment(@RequestBody departmentDto: DepartmentDto): ResponseEntity<Unit> {
        departmentService.addDepartment(departmentDto)

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteDepartment(@PathVariable id: Long): ResponseEntity<Unit> {
        departmentService.deleteDepartment(id)

        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}/rules")
    fun updateRules(@PathVariable id: Long, @RequestBody department: DepartmentDto): ResponseEntity<Unit> {
        departmentService.updatePriority(
            id, department.priority
        )

        departmentService.updateSuccessors(
            id, department.successors.map { departmentService.getDepartmentById(it) }
        )

        departmentService.updateRules(
            id, department.rules.map { it.toDomain() }
        )

        return ResponseEntity.noContent().build()
    }
}
