package basic;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CRUD {
	
	 @Test 
	    public void testCreateUserWithApiKey() {
	        String requestBody = "{\n" +
	                "  \"name\": \"Jone Mark\",\n" +
	                "  \"job\": \"QA Engineer\"\n" +
	                "}";

	        Response response = given()
	                .baseUri("https://reqres.in")
	                .header("Content-Type", "application/json")
	                .header("x-api-key", "reqres-free-v1")   // ✅ required header
	                .body(requestBody)
	        .when()
	                .post("/api/users")
	        .then()
	                .statusCode(201)   // ✅ expect 201 Created
	                .log().all()
	                .extract().response();

	        // assertions
	        assertEquals(response.jsonPath().getString("name"), "Jone Mark");
	        assertEquals(response.jsonPath().getString("job"), "QA Engineer");
	    } 
	    
	    
	   

	        private static String userId;   
	        // Create User
	        @Test(priority = 1)
	        public void testCreateUser() {
	            String requestBody = "{ \"name\": \"Jone Mark\", \"job\": \"QA Engineer\" }";

	            Response response = given()
	                    .baseUri("https://reqres.in")
	                    .contentType(ContentType.JSON)
	                    .header("x-api-key", "reqres-free-v1")
	                    .body(requestBody)
	            .when()
	                    .post("/api/users")
	            .then()
	                    .statusCode(201)
	                    .log().all()
	                    .extract().response();

	            userId = response.jsonPath().getString("id");
	            Assert.assertNotNull(userId, "User ID should not be null after creation");
	        }

	        // Update User (PUT)
	        @Test(priority = 2, dependsOnMethods = "testCreateUser")
	        public void testUpdateUser() {
	            String requestBody = "{ \"name\": \"Neo\", \"job\": \"Zion\" }";

	            Response response = given()
	                    .baseUri("https://reqres.in")
	                    .contentType(ContentType.JSON)
	                    .header("x-api-key", "reqres-free-v1")
	                    .body(requestBody)
	            .when()
	                    .put("/api/users/" + userId)
	            .then()
	                    .statusCode(200)
	                    .log().all()
	                    .extract().response();

	            Assert.assertEquals(response.jsonPath().getString("name"), "Neo");
	            Assert.assertEquals(response.jsonPath().getString("job"), "Zion");
	        }

	        // Partial Update (PATCH)
	        @Test(priority = 3, dependsOnMethods = "testUpdateUser")
	        public void testPatchUser() {
	            String requestBody = "{ \"name\": \"Neo Patched\", \"job\": \"Architect\" }";

	            Response response = given()
	                    .baseUri("https://reqres.in")
	                    .contentType(ContentType.JSON)
	                    .header("x-api-key", "reqres-free-v1")
	                    .body(requestBody)
	            .when()
	                    .patch("/api/users/" + userId)
	            .then()
	                    .statusCode(200)
	                    .log().all()
	                    .extract().response();

	            Assert.assertEquals(response.jsonPath().getString("name"), "Neo Patched");
	            Assert.assertEquals(response.jsonPath().getString("job"), "Architect");
	        }

	        // Delete User
	        @Test(priority = 4, dependsOnMethods = "testPatchUser")
	        public void testDeleteUser() {
	            given()
	                    .baseUri("https://reqres.in")
	                    .header("x-api-key", "reqres-free-v1")
	            .when()
	                    .delete("/api/users/" + userId)
	            .then()
	                    .statusCode(204)
	                    .log().all();
	        }

	        // Verify Deleted User (GET should fail)
	        @Test(priority = 5, dependsOnMethods = "testDeleteUser")
	        public void testGetDeletedUser() {
	            given()
	                    .baseUri("https://reqres.in")
	                    .header("x-api-key", "reqres-free-v1")
	            .when()
	                    .get("/api/users/" + userId)
	            .then()
	                    .statusCode(404)   // expected after delete
	                    .log().all();
	        }
	  

	    

}
	    


 
