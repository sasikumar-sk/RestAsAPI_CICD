package reqres;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.Test;

public class GetTest {

    @Test
    public void testGetUsers() {
        given()
            .baseUri("https://reqres.in")               // Base URI
            .basePath("/api/users")                     // Base Path
            .queryParam("page", 1)                      // Query parameter
            .header("x-api-key", "reqres-free-v1")      // Custom API key header
        .when()
            .get()                                      // GET request
        .then()
            .statusCode(200)                            // Validate HTTP status
            .header("Content-Type", containsString("application/json")) // Content-Type header
            .header("Server", notNullValue())           // Server header
            .header("Connection", equalTo("keep-alive")) // Connection header

            // Body validations
            .body("page", equalTo(1))
            .body("per_page", greaterThan(0))
            .body("data", notNullValue())
            .body("data.size()", greaterThan(0))
            .body("data[0].id", notNullValue())
            .body("data[0].email", containsString("@reqres.in"))
            .body("data[0].first_name", notNullValue())
            .body("data[0].last_name", notNullValue()) 
            
        .log().all(); 
    }
    
    @Test
    public void testUserNotFound() {
        given()
            .baseUri("https://reqres.in")
            .basePath("/api/users/23")  // Non-existent user
            .header("x-api-key", "reqres-free-v1")
        .when()
            .get()
        .then()
            .statusCode(404)
            .log().all();
    }
    
    @Test
    public void testAllEmails() {
        given()
            .baseUri("https://reqres.in")
            .basePath("/api/users")
            .queryParam("page", 1)
            .header("x-api-key", "reqres-free-v1")
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("data.email", everyItem(containsString("@reqres.in")))
            .log().all();
    }
    @Test
    public void testResponseTime() {
        given()
            .baseUri("https://reqres.in")
            .basePath("/api/users")
            .queryParam("page", 1)
            .header("x-api-key", "reqres-free-v1")
        .when()
            .get()
        .then()
            .statusCode(200)
            .time(lessThan(2000L))  // Response time < 2 seconds
            .log().all();
    }

    @Test
    public void testMissingApiKey() {
        given()
            .baseUri("https://reqres.in")
            .basePath("/api/users")
            .queryParam("page", 1)
        .when()
            .get()
        .then()
            .statusCode(401)  
            .log().all();
    }

}
