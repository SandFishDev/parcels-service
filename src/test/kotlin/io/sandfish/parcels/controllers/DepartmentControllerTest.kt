package io.sandfish.parcels.controllers

import io.restassured.RestAssured.baseURI
import io.restassured.RestAssured.given
import io.sandfish.parcels.dtos.DepartmentDto
import io.sandfish.parcels.dtos.RuleDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@TestMethodOrder(value = OrderAnnotation::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class DepartmentControllerTest {

    private var token = ""

    @BeforeAll
    fun setup() {
        baseURI = "http://localhost:8080"
        token = given()
            .contentType("application/json")
            .body("{\n	\"username\": \"admin\",\n	\"password\": \"admin\"\n}")
            .post("api/users/login")
            .then().assertThat().statusCode(200)
            .extract().path("token")
    }


    @Test
    @Order(1)
    fun `department can be created`() {
        val departmentA = DepartmentDto(null, "Test A", 1L, emptySet(), emptySet())
        val departmentB = DepartmentDto(null, "Test B", 1L, emptySet(), emptySet())

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer $token")
            .body(departmentA)
            .post("/api/departments")
            .then()
            .assertThat()
            .statusCode(204)

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer $token")
            .body(departmentB)
            .post("/api/departments")
            .then()
            .assertThat()
            .statusCode(204)
    }

    @Test
    @Order(2)
    fun `department can be updated`() {
        val department = DepartmentDto(1, "Mail", 3L,
            successors = setOf(2L),
            rules = setOf(RuleDto("value", ">", 10.0))
        )

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer $token")
            .body(department)
            .put("/api/departments/1")
            .then()
            .assertThat()
            .statusCode(204)
    }

    @Test
    @Order(3)
    fun `department can get retrieved by database id`() {
        given()
            .header("Authorization", "Bearer $token")
            .get("/api/departments/1")
            .then()
            .assertThat()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", equalTo("Mail"))
            .body("priority", equalTo(3))
            .body("successors.size()", equalTo(1))
            .body("rules.size()", equalTo(1))
    }

    @Test
    @Order(4)
    fun `all departments can be retrieved`() {
        given()
            .header("Authorization", "Bearer $token")
            .get("/api/departments")
            .then()
            .assertThat()
            .statusCode(200)
            .body("size()", equalTo(6))

    }

    @Test
    @Order(5)
    fun `department can be deleted`() {
        given()
            .header("Authorization", "Bearer $token")
            .delete("/api/departments/1")
            .then()
            .assertThat()
            .statusCode(204)
    }
}
