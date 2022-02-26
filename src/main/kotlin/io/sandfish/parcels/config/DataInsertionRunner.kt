package io.sandfish.parcels.config

import io.sandfish.parcels.controllers.DepartmentController
import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.domain.Rule
import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.UserDto
import io.sandfish.parcels.services.authentication.RoleService
import io.sandfish.parcels.services.authentication.UserService
import io.sandfish.parcels.services.department.DepartmentService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Class to insert the default departments and some users for easy testing
 * Ordinarily I would add Liquibase and insert stuff from there but I don"t feel like doing that today.
 */
@Component
@Profile("!test")
class DataInsertionRunner(
    val departmentService: DepartmentService,
    val roleService: RoleService,
    val userService: UserService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        //Add departments
        val mailDepartment = departmentService.addDepartment(
            DepartmentDto(null, name = "Mail", 2, setOf(), setOf())
        )
        departmentService.updateRules(
            mailDepartment.id!!,
            listOf(
                Rule("weight", "<=", 1.00)
            )
        )

        val regularDepartment = departmentService.addDepartment(
            DepartmentDto(null, name = "Regular", 2, setOf(), setOf())
        )
        departmentService.updateRules(
            regularDepartment.id!!,
            listOf(
                Rule("weight", ">", 1.00),
                Rule("weight", "<=", 10.00)
            )
        )

        val heavyDepartment = departmentService.addDepartment(
            DepartmentDto(null, name = "Heavy", 2, setOf(), setOf())
        )
        departmentService.updateRules(
            heavyDepartment.id!!,
            listOf(
                Rule("weight", ">", 10.00)
            )
        )

        val insuranceDepartment = departmentService.addDepartment(
            DepartmentDto(null, name = "Insurance", 1, setOf(1, 2, 3), setOf())
        )
        departmentService.updateRules(
            insuranceDepartment.id!!,
            listOf(
                Rule("value", ">", 1000.0)
            )
        )

        departmentService.updateSuccessors(insuranceDepartment.id, setOf(
            mailDepartment.id, regularDepartment.id, heavyDepartment.id
        ))

        //Add standard roles manually
        val userRole = Role(id = null, name = "USER", description = "Standard role for every user.", users = mutableSetOf())
        val adminRole = Role(id = null, name = "ADMIN", description = "Admin role.", users = mutableSetOf())

        //These are already created during the creation of the department themselves
        val mailRole = roleService.findByName("MAIL")
        val regularRole = roleService.findByName("REGULAR")
        val heavyRole = roleService.findByName("HEAVY")
        val insuranceRole = roleService.findByName("INSURANCE")

        roleService.createRole(userRole)
        roleService.createRole(adminRole)

        //Add a bunch of users to more easily test
        val adminUser = userService.save(UserDto(username = "admin", password = "admin"))
        adminUser.roles = mutableSetOf(userRole, adminRole)
        userService.updateRoles(adminUser.id!!, adminUser)

        val mailParcelUser = userService.save(UserDto(username = "mail", password = "mail"))
        mailParcelUser.roles = mutableSetOf(userRole,mailRole)
        userService.updateRoles(mailParcelUser.id!!, mailParcelUser)


        val regularParcelUser = userService.save(UserDto(username = "regular", password = "regular"))
        regularParcelUser.roles = mutableSetOf(userRole,  regularRole, )
        userService.updateRoles(regularParcelUser.id!!, regularParcelUser)


        val heavyParcelUser = userService.save(UserDto(username = "heavy", password = "heavy"))
        heavyParcelUser.roles = mutableSetOf(userRole,  heavyRole)
        userService.updateRoles(heavyParcelUser.id!!, heavyParcelUser)


        val insuranceParcelUser = userService.save(UserDto(username = "insurance", password = "insurance"))
        insuranceParcelUser.roles = mutableSetOf(userRole,insuranceRole)
        userService.updateRoles(insuranceParcelUser.id!!, insuranceParcelUser)


        val superadminUser = userService.save(UserDto(username = "superadmin", password = "superadmin"))
        superadminUser.roles = mutableSetOf(userRole, adminRole, mailRole, regularRole, heavyRole, insuranceRole)
        userService.updateRoles(superadminUser.id!!, superadminUser)
    }
}
