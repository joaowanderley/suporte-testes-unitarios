package com.example.demo.integrationtest.swagger;

import com.example.demo.config.TestConfigs;
import com.example.demo.integrationtest.testcontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void testShouldDisplaySwaggerUiPage() {
        var content = given().basePath("/swagger-ui/index.html")
                .port(TestConfigs.SERVER_PORT)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().body().asString();

        assertTrue(content.contains("Swagger UI"));
    }

}
