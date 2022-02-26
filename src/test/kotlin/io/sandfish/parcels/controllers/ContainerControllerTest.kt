package io.sandfish.parcels.controllers

import io.restassured.RestAssured.baseURI
import io.restassured.RestAssured.given
import io.sandfish.parcels.domain.Container
import io.sandfish.parcels.repositories.ContainerRepository
import io.sandfish.parcels.repositories.ParcelRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ResourceLoader
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.equalTo
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ContainerControllerTest {

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @Autowired
    private lateinit var containerRepository: ContainerRepository

    @Autowired
    private lateinit var parcelRepository: ParcelRepository

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
    fun `test container xml parsing`() {
        val file = resourceLoader.getResource("classpath:Container_68465468.xml").file

        given()
            .contentType("application/xml")
            .header("Authorization", "Bearer $token")
            .body(file)
            .post("/api/containers")
            .then()
            .assertThat()
            .statusCode(204)

        assertThat(containerRepository.count()).isOne
        assertThat(containerRepository.findById(1L).get().containerId).isEqualTo(68465468)

        assertThat(parcelRepository.count()).isEqualTo(17)
    }

    @Test
    fun `container can be retrieved by database id`(){
        given()
            .header("Authorization", "Bearer $token")
            .get("/api/containers/1")
            .then()
            .assertThat()
            .statusCode(200)
            .body("id", equalTo(1) )
            .body("containerId", equalTo(68465468) )
            .body("shippingDate", equalTo("2016-07-22T00:00:00+02:00") )
    }

    @Test
    fun `all containers can be retrieved`(){
        given()
            .header("Authorization", "Bearer $token")
            .get("/api/containers")
            .then()
            .assertThat()
            .statusCode(200)
            .body("size()", equalTo(1) )
    }

    @Test
    fun `container statistics can be retrieved`(){
        given()
            .header("Authorization", "Bearer $token")
            .get("/api/containers/statistics")
            .then()
            .assertThat()
            .statusCode(200)
            .body("containerCount", equalTo(1) )
            .body("containerCountLast24Hours", equalTo(1) )
    }
}
