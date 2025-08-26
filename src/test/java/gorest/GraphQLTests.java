package gorest;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class GraphQLTests extends BaseTest {

    private int createdUserId;

    // Simple static query
    @Test(priority = 1)
    public void testQueryUsersStatic() {
        JSONObject request = new JSONObject();
        request.put("query", "query{users {nodes {id name email gender status}}}");

        Response response = given()
                .body(request.toString())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Static Query Users:\n" + response.asPrettyString());
    }

    // Create user using dynamic input
    @Test(priority = 2)
    public void testCreateUserDynamic() {
        String name = "Sasi Test1";
        String email = "sasi+" + System.currentTimeMillis() + "@test.com";

        JSONObject request = new JSONObject();
        request.put("query", "mutation{createUser(input: {name: \"" + name + "\", gender: \"male\", email: \"" + email + "\", status: \"active\"}) {user{id name email}}}");

        Response response = given()
                .body(request.toString())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().response();

        createdUserId = response.jsonPath().getInt("data.createUser.user.id");
        assertEquals(response.jsonPath().getString("data.createUser.user.name"), name);

        System.out.println("Created User Dynamic:\n" + response.asPrettyString());
    }

    // Query user by ID using variables
    @Test(priority = 3, dependsOnMethods = "testCreateUserDynamic")
    public void testQueryUserByIdWithVariables() {
        JSONObject request = new JSONObject();
        request.put("query", "query($id: ID!) { user(id: $id) {id name email gender status} }");

        JSONObject variables = new JSONObject();
        variables.put("id", createdUserId);
        request.put("variables", variables);

        Response response = given()
                .body(request.toString())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Query User by ID with Variables:\n" + response.asPrettyString());
    }

    // Update user using DataProvider
    @DataProvider(name = "updateUserData")
    public Object[][] updateUserData() {
        return new Object[][]{
                {"Sasi Updated - 1"},
                {"Sasi name Updated -2"},
        };
    }

    @Test(priority = 4, dependsOnMethods = "testCreateUserDynamic", dataProvider = "updateUserData")
    public void testUpdateUserDataProvider(String newName) {
        JSONObject request = new JSONObject();
        request.put("query", "mutation{updateUser(input: {id: " + createdUserId + ", name: \"" + newName + "\"}) {user{id name}}}");

        Response response = given()
                .body(request.toString())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals(response.jsonPath().getString("data.updateUser.user.name"), newName);
        System.out.println("Updated User via DataProvider:\n" + response.asPrettyString());
    }
    
    // Verify user after updated
    @Test(priority = 5, dependsOnMethods = "testUpdateUserDataProvider")
    public void testUpdateUserAfterUpdate() {
        JSONObject request = new JSONObject();
        request.put("query", "query($id: ID!) { user(id: $id) {id name email} }");

        JSONObject variables = new JSONObject();
        variables.put("id", createdUserId);
        request.put("variables", variables);

        Response response = given()
                .body(request.toString())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().response();

        // Fetch the current name after update
        String currentName = response.jsonPath().getString("data.user.name"); 
        assertEquals(currentName, "Sasi name Updated -2", "User name should match the last updated name");
        System.out.println("Verified User Name after Update:\n" + response.asPrettyString());
    }
    

    // Delete user
    @Test(priority = 6, dependsOnMethods = "testCreateUserDynamic")
    public void testDeleteUser() {
        JSONObject request = new JSONObject();
        request.put("query", "mutation{deleteUser(input: {id: " + createdUserId + "}) {user{id name}}}");

        Response response = given()
                .body(request.toString())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("Deleted User:\n" + response.asPrettyString());
    }

    // Verify user after deletion
    @Test(priority = 7, dependsOnMethods = "testDeleteUser")
    public void testGetUserAfterDelete() {
        JSONObject request = new JSONObject();
        request.put("query", "query($id: ID!) { user(id: $id) {id name email} }");

        JSONObject variables = new JSONObject();
        variables.put("id", createdUserId);
        request.put("variables", variables);

        Response response = given()
                .body(request.toString())
                .post("/graphql")
                .then()
                .statusCode(200)
                .extract().response();

        // user should be null
        Object user = response.jsonPath().get("data.user");
        assertNull(user, "User should not exist after deletion");

        System.out.println("Verified User Deletion (should be null):\n" + response.asPrettyString());
    }
}
 