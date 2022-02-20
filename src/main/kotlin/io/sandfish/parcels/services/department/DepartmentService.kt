package io.sandfish.parcels.services.department

import io.sandfish.parcels.domain.Department
import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.repositories.DepartmentRepository
import io.sandfish.parcels.services.authentication.RoleService
import org.springframework.stereotype.Service


@Service
class DepartmentService(
    private val roleService: RoleService,
    private val departmentRepository: DepartmentRepository
) {

    /**
     * Add a new department, also adds matching Role entry
     */
    fun addDepartment(department: DepartmentDto) {
        departmentRepository.save(Department(null, name = department.name))

        roleService.addRole(
            RoleDto(
                name = department.name.uppercase(),
                description = "Role for ${department.name} department."
            )
        )
    }

    fun getDepartments(): List<Department> {
        return departmentRepository.findAll().toList()
    }
}
