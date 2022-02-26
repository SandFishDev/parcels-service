package io.sandfish.parcels.services.department

import io.sandfish.parcels.domain.Department
import io.sandfish.parcels.domain.Rule
import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.repositories.DepartmentRepository
import io.sandfish.parcels.repositories.RoleRepository
import io.sandfish.parcels.repositories.RuleRepository
import io.sandfish.parcels.services.authentication.RoleService
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class DepartmentService(
    private val roleService: RoleService,
    private val departmentRepository: DepartmentRepository,
    private val roleRepository: RoleRepository,
    private val ruleRepository: RuleRepository,
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

        roleService.addRole(
            RoleDto(
                name = department.name.uppercase(),
                description = "Role for ${department.name} department."
            )
        )

        return departmentRepository.save(savedDepartment)
    }


    fun updateSuccessors(id: Long, successors: List<Department>) {
        val department = departmentRepository.findById(id).orElseThrow { RuntimeException() }

        //remove current ancestory from other
        department.successors.forEach{
            it.ancestors.remove(department)
        }
        //remova ancestory
        department.successors.clear()


        //add ancestor to every successors
        successors.forEach{
            it.ancestors.add(department)
        }

        //add successors
        department.successors.addAll(successors)

        departmentRepository.save(department)
    }

    fun updateRules(id: Long, rules: List<Rule>) {
        val department = departmentRepository.findById(id).orElseThrow { RuntimeException() }

        rules.map { it.department = department; it }

        department.rules.clear()
        department.rules.addAll(rules)

        departmentRepository.save(department)
    }

    fun updatePriority(id: Long, priority: Long) {
        val department = departmentRepository.findById(id).orElseThrow { RuntimeException() }

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
     * Delete department and it's associated role
     */
    @Transactional
    fun deleteDepartment(id: Long) {
        val department = departmentRepository.findById(id)

        if (department.isPresent) {
            val toBeDeletedRole = roleRepository.findByName(department.get().name.uppercase())
            toBeDeletedRole.users.forEach{
                it.roles.remove(toBeDeletedRole)
            }

            roleRepository.deleteById(toBeDeletedRole.id)
            departmentRepository.deleteById(id)
        }
    }
}
