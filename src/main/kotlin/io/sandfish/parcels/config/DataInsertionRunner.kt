package io.sandfish.parcels.config

import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.dtos.UserDto
import io.sandfish.parcels.services.authentication.RoleService
import io.sandfish.parcels.services.authentication.UserService
import io.sandfish.parcels.services.department.DepartmentService
import io.sandfish.parcels.services.department.DepartmentType
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * Class to insert the default departments and some users for easy testing
 * Ordinarily I would add Liquibase and insert stuff from there but I don't feel like doing that today.
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
            DepartmentDto(name = DepartmentType.Mail.name)
        )
        departmentService.addDepartment(
            DepartmentDto(name = DepartmentType.Regular.name)
        )
        departmentService.addDepartment(
            DepartmentDto(name = DepartmentType.Heavy.name)
        )

        departmentService.addDepartment(
            DepartmentDto(null, name = DepartmentType.Insurance.name, setOf(1, 2, 3))
        )


        val mailRole = RoleDto("MAIL")
        val regularRole = RoleDto("REGULAR")
        val heavyRole = RoleDto("HEAVY")
        val insuranceRole = RoleDto("INSURANCE")

        //Add a bunch of users to more easily test
        val adminUser = userService.save(UserDto(username = "admin", password = "admin"))
        userService.addRoleToUser(adminUser.id!!, adminRole)

        val mailParcelUser = userService.save(UserDto(username = "mail", password = "mail"))
        userService.addRoleToUser(mailParcelUser.id!!, mailRole)

        val regularParcelUser = userService.save(UserDto(username = "regular", password = "regular"))
        userService.addRoleToUser(regularParcelUser.id!!, regularRole)

        val heavyParcelUser = userService.save(UserDto(username = "heavy", password = "heavy"))
        userService.addRoleToUser(heavyParcelUser.id!!, heavyRole)

        val insuranceParcelUser = userService.save(UserDto(username = "insurance", password = "insurance"))
        userService.addRoleToUser(insuranceParcelUser.id!!, insuranceRole)

        val superadminUser = userService.save(UserDto(username = "superadmin", password = "superadmin"))
        userService.addRoleToUser(superadminUser.id!!, adminRole)
        userService.addRoleToUser(superadminUser.id, mailRole)
        userService.addRoleToUser(superadminUser.id, regularRole)
        userService.addRoleToUser(superadminUser.id, heavyRole)
        userService.addRoleToUser(superadminUser.id, insuranceRole)
    }
}
