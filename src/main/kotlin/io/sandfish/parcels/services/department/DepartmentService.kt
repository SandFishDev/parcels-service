package io.sandfish.parcels.services.department

import io.sandfish.parcels.domain.Department
import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.repositories.DepartmentRepository
import io.sandfish.parcels.repositories.RoleRepository
import io.sandfish.parcels.services.authentication.RoleService
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class DepartmentService(
    private val roleService: RoleService,
    private val departmentRepository: DepartmentRepository,
    private val roleRepository: RoleRepository
) {

    /**
     * Add a new department, also adds matching Role entry
     */
    fun addDepartment(department: DepartmentDto) {
        val savedDepartment = departmentRepository.save(
            Department(
                null,
                name = department.name,
                successors = mutableSetOf(),
                ancestors = mutableSetOf()
            )
        )

        val successorDepartments = department.successors.map {
            val dep = departmentRepository.findById(it).get()
            dep.ancestors.add(savedDepartment)
            dep
        }

        savedDepartment.successors.addAll(successorDepartments)

        departmentRepository.save(savedDepartment)


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

    fun getDepartmentById(id: Long): Department {
        return departmentRepository.findById(id).get()
    }

    fun getDepartmentByName(name: String): Department {
        return departmentRepository.findByName(name)
    }

    /**
     * Delete department and it's associated role
     */
    @Transactional
    fun deleteDepartment(id: Long) {
        val department = departmentRepository.findById(id)

        if (department.isPresent) {
            roleRepository.deleteByName(department.get().name.uppercase())
            departmentRepository.deleteById(id)
        }
    }


}
