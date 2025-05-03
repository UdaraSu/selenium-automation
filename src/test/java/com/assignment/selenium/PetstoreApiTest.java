package com.assignment.selenium;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PetstoreApiTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test
    @Order(1)
    void testAddNewPet() {
        String requestBody = """
            {
              "id": 123456789,
              "name": "Snowball",
              "status": "available"
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/pet")
            .then()
            .statusCode(200)
            .body("name", equalTo("Snowball"))
            .body("status", equalTo("available"));
    }

    @Test
    @Order(2)
    void testGetPetById() {
        given()
            .when()
            .get("/pet/123456789")
            .then()
            .statusCode(200)
            .body("id", equalTo(123456789));
    }

    @Test
    @Order(3)
    void testUpdatePet() {
        String requestBody = """
            {
              "id": 123456789,
              "name": "Snowball",
              "status": "sold"
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .put("/pet")
            .then()
            .statusCode(200)
            .body("status", equalTo("sold"));
    }

    @Test
    @Order(4)
    void testDeletePet() {
        given()
            .when()
            .delete("/pet/123456789")
            .then()
            .statusCode(200);
    }
}
