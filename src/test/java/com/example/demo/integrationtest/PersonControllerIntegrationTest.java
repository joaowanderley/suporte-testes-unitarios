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

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        person.setFirstNome("asat");
        person.setLastNome("passati");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract().body().asString();

        Person updatedPerson = objectMapper.readValue(content, Person.class);

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getFirstNome());
    }

    @Test
    @Order(3)
    void testIntegrationTestGivenPersonId_whenFindByIdPerson_ShouldReturnPersonObject() throws JsonProcessingException {
        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract().body().asString();

        person = objectMapper.readValue(content, Person.class);

        assertNotNull(person);
    }

    @Test
    @Order(4)
    void testIntegrationTest_whenFindAllPerson_ShouldReturnPersonsList() throws JsonProcessingException {

        Person newPerson = new Person("any", "anyLast", "anyAddress", "anyGender", "any@email.com");
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(newPerson)
                .when()
                .post()
                .then()
                .statusCode(200);

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        List<Person> personList = Arrays.asList(objectMapper.readValue(content, Person[].class));

        assertNotNull(personList.getFirst());
        assertEquals(2, personList.size());
    }

    @Test
    @Order(5)
    void testIntegrationTestGivenPersonId_whenDeletePerson_ShouldReturnStatusNoContent() {
        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }
}