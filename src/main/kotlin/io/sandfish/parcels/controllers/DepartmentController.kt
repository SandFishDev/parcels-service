package io.sandfish.parcels.controllers

import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.services.department.DepartmentService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * RestController for endpoint related to departments
 */
@RestController
@RequestMapping("/api/departments")
class DepartmentController(
    val departmentService: DepartmentService
) {

    /**
     * Get all departments
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getDepartments(): List<DepartmentDto> {
        return departmentService.getDepartments().map { it.toTransferObject() }
    }

    /**
     * Get a department by id
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    fun getDepartmentById(@PathVariable id: Long): DepartmentDto {
        return departmentService.getDepartmentById(id).toTransferObject()
    }

    /**
     * Create a new department
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createDepartment(@RequestBody departmentDto: DepartmentDto): ResponseEntity<Unit> {
        departmentService.addDepartment(departmentDto)

        return ResponseEntity.noContent().build()
    }

    /**
     * Delete a existing department
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteDepartment(@PathVariable id: Long): ResponseEntity<Unit> {
        departmentService.deleteDepartment(id)

        return ResponseEntity.noContent().build()
    }

    /**
     * Update the priority, successors and rules of an existing department
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateDepartment(@PathVariable id: Long, @RequestBody department: DepartmentDto): ResponseEntity<Unit> {
        departmentService.updatePriority(
            id, department.priority
        )

        departmentService.updateSuccessors(
            id, department.successors
        )

        departmentService.updateRules(
            id, department.rules.map { it.toDomain() }
        )

        return ResponseEntity.noContent().build()
    }
}
