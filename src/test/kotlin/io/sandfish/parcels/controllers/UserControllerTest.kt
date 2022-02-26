package io.sandfish.parcels.controllers

import io.restassured.RestAssured
import io.sandfish.parcels.domain.Role
import io.sandfish.parcels.dtos.RoleDto
import io.sandfish.parcels.dtos.UserDto
import io.sandfish.parcels.dtos.UserWithRolesDto
import io.sandfish.parcels.repositories.ParcelRepository
import io.sandfish.parcels.services.authentication.RoleService
import io.sandfish.parcels.services.authentication.UserService
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@TestMethodOrder(value = MethodOrderer.OrderAnnotation::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@Order(1)
internal class UserControllerTest {

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var userService: UserService

    private var token = ""

    @BeforeAll
    fun setup() {
        RestAssured.baseURI = "http://localhost:8080"

        roleService.createRole(
            Role(null, "USER", "Default Role", mutableSetOf())
        )

        val adminRole = roleService.createRole(
            Role(null, "ADMIN", "Default Role", mutableSetOf())
        )

        val adminuser = userService.save(UserDto("admin", "admin"))
        adminuser.roles.add(adminRole)
        userService.updateRoles(adminuser.id!!,  adminuser)

        token = RestAssured.given()
            .contentType("application/json")
            .body("{\n	\"username\": \"admin\",\n	\"password\": \"admin\"\n}")
            .post("api/users/login")
            .then().assertThat().statusCode(200)
            .extract().path("token")
    }

    @Test
    fun saveUser() {
        val user = UserDto("user", "user")

        RestAssured.given()
            .contentType("application/json")
            .body(user)
            .post("/api/users/register")
            .then()
            .assertThat()
            .statusCode(200)
    }

    @Test
    fun updateUser() {
        val user = UserWithRolesDto(2, "user", mutableSetOf(
            RoleDto(2, "ADMIN", null)
        ))

        RestAssured.given()
            .contentType("application/json")
            .header("Authorization", "Bearer $token")
            .body(user)
            .put("/api/users/2")
            .then()
            .assertThat()
            .statusCode(200)
    }
}
