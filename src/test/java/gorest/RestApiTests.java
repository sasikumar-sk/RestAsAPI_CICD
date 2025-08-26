package gorest;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class RestApiTests extends BaseTest {

    private int createdUserId;

    @Test(priority = 1)
    public void testCreateUser() {
        JSONObject request = new JSONObject(); //Input type: JSON objects
        request.put("name", "sasi");
        request.put("gender", "male");
        request.put("email", "sasi+" + System.currentTimeMillis() + "@example.com");
        request.put("status", "active");

        Response response = given()
                .body(request.toString())
                .post("/users")
                .then()
                .statusCode(201)
                .extract().response();

        createdUserId = response.jsonPath().getInt("id");
        System.out.println("Created User ID: " + createdUserId);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateUser")
    public void testGetUser() {
        Response response = given()
                .get("/users/" + createdUserId)
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asPrettyString());
    }

    @Test(priority = 3, dependsOnMethods = "testCreateUser")
    public void testUpdateUser() {
        String updatedName = "Sasi Kumar - (Updated)";
        
        JSONObject request = new JSONObject();
        request.put("name", updatedName);

        Response response = given()
                .body(request.toString())
                .patch("/users/" + createdUserId)
                .then()
                .statusCode(200)
                .extract().response();

        // Assertion to validate the name was updated
        String actualName = response.jsonPath().getString("name");
        assertEquals(actualName, updatedName, "User name should be updated successfully");

        System.out.println("Updated User Details: " + response.asPrettyString());
    }


    @Test(priority = 4, dependsOnMethods = "testCreateUser")
    public void testDeleteUser() {
        given()
                .delete("/users/" + createdUserId)
                .then()
                .statusCode(204);

        System.out.println("User deleted successfully");
    }
    
    @Test(priority = 5, dependsOnMethods = "testDeleteUser")
    public void testGetUserAfterDelete() {
        Response response = given()
                .get("/users/" + createdUserId)
                .then()
                .statusCode(404) // still 404
                .extract().response();

        String message = response.jsonPath().getString("message");
        assertEquals(message, "Resource not found");

        System.out.println("Verified user deletion: " + message);
    }

    
}
