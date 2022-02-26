package io.sandfish.parcels.services.department

import io.sandfish.parcels.controllers.exceptions.EntityNotFoundException
import io.sandfish.parcels.domain.Department
import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.domain.Rule
import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.repositories.DepartmentRepository
import io.sandfish.parcels.repositories.RoleRepository
import io.sandfish.parcels.services.authentication.RoleService
import org.springframework.stereotype.Service


@Service
class DepartmentService(
    private val roleService: RoleService,
    private val departmentRepository: DepartmentRepository,
    private val roleRepository: RoleRepository,
) {

    /**
     * Add a new department, also adds matching Role entry
     */
    fun addDepartment(department: DepartmentDto): Department {
        val savedDepartment = departmentRepository.save(
            Department(
                null,
                name = department.name,
                successors = mutableSetOf(),
                ancestors = mutableSetOf(),
                rules = mutableSetOf(),
                priority = department.priority
            )
        )

        val successorDepartments = department.successors.map {
            val dep = departmentRepository.findById(it).get()
            dep.ancestors.add(savedDepartment)
            dep
        }

        savedDepartment.successors.addAll(successorDepartments)

        roleService.createRole(
            Role(
                id = null,
                name = department.name.uppercase(),
                description = "Role for ${department.name} department.",
                users = mutableSetOf()
            ),
        )

        return departmentRepository.save(savedDepartment)
    }


    fun updateSuccessors(id: Long, successors: Set<Long>) {
        val department = departmentRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Department with id '$id' could not be found.") }
        val actualSuccesors = successors.map { this.getDepartmentById(it) }

        //remove current ancestory from other
        department.successors.forEach {
            it.ancestors.remove(department)
        }

        //remova ancestory
        department.successors.clear()


        //add ancestor to every successors
        actualSuccesors.forEach {
            it.ancestors.add(department)
        }

        //add successors
        department.successors.addAll(actualSuccesors)

        departmentRepository.save(department)
    }

    fun updateRules(id: Long, rules: List<Rule>) {
        val department = departmentRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Department with id '$id' could not be found.") }

        rules.map { it.department = department; it }

        department.rules.clear()
        department.rules.addAll(rules)

        departmentRepository.save(department)
    }

    fun updatePriority(id: Long, priority: Long) {
        val department = departmentRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Department with id '$id' could not be found.") }

        department.priority = priority

        departmentRepository.save(department)
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
     * Delete the department and its associated role and remove the role from all users
     * For now we consider parcels associated with this department lost, and they cannot be processed without database manipulation
     */
    fun deleteDepartment(id: Long) {
        val department = departmentRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Department with id '$id' could not be found.") }

        val toBeDeletedRole = roleRepository.findByName(department.name.uppercase())
        toBeDeletedRole.users.forEach {
            it.roles.remove(toBeDeletedRole)
        }
        roleRepository.deleteById(toBeDeletedRole.id!!)

        //remove successors reference
        department.successors.forEach {
            it.ancestors.remove(department)
            departmentRepository.save(it)
        }
        department.successors.clear()

        //remove ancestors reference
        department.ancestors.forEach {
            it.successors.remove(department)
            departmentRepository.save(it)
        }
        department.ancestors.clear()


        departmentRepository.delete(department)
    }
}
