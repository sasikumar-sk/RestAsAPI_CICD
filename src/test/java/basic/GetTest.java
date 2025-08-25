package basic;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.testng.annotations.Test;

public class GetTest {

  @Test()
   public void testGetUsers() { 
      
                given()
                .when()
                	.get("https://reqres.in/api/users")
                .then()
                	.statusCode(200)   
                    .header("Content-Type", containsString("application/json"))

                	  .body("page", equalTo(1))  
                      .body("per_page", equalTo(6)) 
                      .body("data", notNullValue()) 
                      .body("data.size()", greaterThan(0)) 
                      .body("data[0].id", notNullValue())  
                      .body("data[0].email", containsString("@reqres.in"))    
                	.log().all();
        
    } 
  
   
    }

    
    