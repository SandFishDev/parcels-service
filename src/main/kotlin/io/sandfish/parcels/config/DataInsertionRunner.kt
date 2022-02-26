package io.sandfish.parcels.config

import io.sandfish.parcels.domain.Rule
import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.dtos.UserDto
import io.sandfish.parcels.services.authentication.RoleService
import io.sandfish.parcels.services.authentication.UserService
import io.sandfish.parcels.services.department.DepartmentService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * Class to insert the default departments and some users for easy testing
 * Ordinarily I would add Liquibase and insert stuff from there but I don"t feel like doing that today.
 */
@Component
class DataInsertionRunner(
    val departmentService: DepartmentService,
    val roleService: RoleService,
    val userService: UserService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        //Add standard roles manually
        val userRole = RoleDto(
            name = "USER",
            description = "Standard role for every user."
        )

        val adminRole = RoleDto(
            name = "ADMIN",
            description = "Admin role."
        )

        roleService.addRole(userRole)
        roleService.addRole(adminRole)

        //Add departments
        departmentService.addDepartment(
            DepartmentDto(null, name = "Mail", 2, setOf(), setOf())
        )
        departmentService.updateRules(
            1,
            listOf(
                Rule("weight", "<=", 1.00)
            )
        )


        departmentService.addDepartment(
            DepartmentDto(null, name = "Regular", 2, setOf(), setOf())
        )
        departmentService.updateRules(
            2,
            listOf(
                Rule("weight", ">", 1.00),
                Rule("weight", "<=", 10.00)
            )
        )


        departmentService.addDepartment(
            DepartmentDto(null, name = "Heavy", 2, setOf(), setOf())
        )
        departmentService.updateRules(
            3,
            listOf(
                Rule("weight", ">", 10.00)
            )
        )

        departmentService.addDepartment(
            DepartmentDto(null, name = "Insurance", 1, setOf(1, 2, 3), setOf())
        )

        departmentService.updateRules(
            4,
            listOf(
                Rule("value", ">", 1000.0)
            )
        )

        val mailRole = RoleDto("MAIL")
        val regularRole = RoleDto("REGULAR")
        val heavyRole = RoleDto("HEAVY")
        val insuranceRole = RoleDto("INSURANCE")

        //Add a bunch of users to more easily test
        val adminUser = userService.save(UserDto(username = "admin", password = "admin"))
        val mailParcelUser = userService.save(UserDto(username = "mail", password = "mail"))
        val regularParcelUser = userService.save(UserDto(username = "regular", password = "regular"))
        val heavyParcelUser = userService.save(UserDto(username = "heavy", password = "heavy"))
        val insuranceParcelUser = userService.save(UserDto(username = "insurance", password = "insurance"))
        val superadminUser = userService.save(UserDto(username = "superadmin", password = "superadmin"))

    }
}
