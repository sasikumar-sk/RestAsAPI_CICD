package sirvApi;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;


//This test will create new auth tokan and then upload image file, download that file, delete file.


public class SirvApiWorkflowTestNG {

    private static String token;
    private static final String CLIENT_ID = "FqPDeN3QUbxw2FvobndWcv96wx5";
    private static final String CLIENT_SECRET = "xl0c7sYyDWC0RIcfvaItMHw7+XiSHKhEbE5TV3f+Ft2C41N2S5j2rgq9W6oN5viaP/Ctq3WKHr4i5CgipqNIug==";

    private static final String BASE_URI = "https://api.sirv.com/v2";
    private static final String TEST_FOLDER = "/rest-assured-test-folder";
    private static final String FILE_NAME = "yourself-wallpaper.jpg";
    private static final String FILE_PATH = TEST_FOLDER + "/" + FILE_NAME;

    @BeforeClass
    public void generateToken() {
        RestAssured.baseURI = BASE_URI;

        token = given()
            .header("Content-Type", "application/json")
            .body("{ \"clientId\": \"" + CLIENT_ID + "\", \"clientSecret\": \"" + CLIENT_SECRET + "\" }")
        .when()
            .post("/token")
        .then()
            .statusCode(200)
            .extract()
            .path("token");

        System.out.println("✅ Access Token generated: ");
    }

    @Test(priority = 1)
    public void uploadFile() {
        File file = new File("src/test/resources/" + FILE_NAME);
        if (!file.exists()) throw new RuntimeException("❌ File not found: " + file.getAbsolutePath());

        given()
            .header("Authorization", "Bearer " + token)
            .multiPart("file", file)
        .when()
            .post("/files/upload?filename=" + FILE_PATH)
        .then()
            .statusCode(200);

        System.out.println("✅ File uploaded successfully: " + FILE_PATH);
    }

    

    @Test(priority = 3)
    public void downloadFile() throws IOException {
        Response response = given()
            .header("Authorization", "Bearer " + token)
            .queryParam("filename", FILE_PATH)
        .when()
            .get("/files/download")
        .then()
            .statusCode(200)
            .extract().response();

        // Save locally
        byte[] fileBytes = response.asByteArray();
        Files.write(Paths.get("target/downloaded-" + FILE_NAME), fileBytes);

        System.out.println("✅ File downloaded locally: target/downloaded-" + FILE_NAME);
    }

    @Test(priority = 4)
    public void deleteFile() {
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .post("/files/delete?filename=" + FILE_PATH)
        .then()
            .statusCode(200);   

        System.out.println("✅ File deleted successfully: " + FILE_PATH);
    }

    } 