package com.example.demo.integrationtest;

import com.example.demo.config.TestConfigs;
import com.example.demo.integrationtest.testcontainers.AbstractIntegrationTest;
import com.example.demo.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static Person person;

    @BeforeAll
    public static void setup() {
        // given / arrange
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
        person = new Person("joao", "paulo", "maceio", "male", "joao@gmail.com");
    }

    @Test
    @Order(1)
    void testIntegrationTest_whenCreatePerson_ShouldReturnPersonObject() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);
        person = createdPerson;

        assertNotNull(person);
        assertNotNull(person.getId());
    }

    @Test
    @Order(2)
    void testIntegrationTestGivenAPersonObject_whenUpdatePerson_ShouldReturnPersonObjectUpdated() throws JsonProcessingException {
        Person updatedPerson = new Person("jhon", "wanderley", "maceio", "male", "joao@mail.com");
        updatedPerson.setId(1L);
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(updatedPerson)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body().asString();

        person = objectMapper.readValue(content, Person.class);

        assertNotNull(person);
        assertNotNull(person.getFirstNome());
    }
}