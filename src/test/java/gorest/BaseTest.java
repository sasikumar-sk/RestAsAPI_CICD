package gorest;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    protected final String BASE_URL = "https://gorest.co.in/public/v2";
    protected final String ACCESS_TOKEN = "d939e9b8c5f02a76196c665893ab6371955682eb1273b97cbc7906355cda2c0c";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.requestSpecification = RestAssured
                .given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }
}
