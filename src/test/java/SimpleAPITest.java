import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleAPITest {
    /**
     * Helper method to get the session id
     *
     * */
    public String getSessionId() throws IOException {
        String url = " http://localhost:8080/rest/auth/1/session";
      //  String payLoad = new String(Files.readAllBytes(Paths.get("/Users/jahidul/IdeaProjects/APITestingBYRestassured/resources/JiraLogin.json")));
        /**
         * Convert JSON object to java object(String object)
         *
         * */
        String payLoad = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+ "/resources/JiraLogin.json")));

        RequestSpecification requestSpecification = RestAssured.given().body(payLoad);
        requestSpecification.contentType("application/json");

        Response response = requestSpecification.post(url);

        String stringResponse = response.asString();


        JsonPath jsonResponse = new JsonPath(stringResponse);
        String jSessionId = jsonResponse.get("session.value");

        System.out.println(jSessionId);
        return  jSessionId;

    }

    @Test
    public void createIssue() throws IOException {
        String jSessionId = getSessionId();
        String url = "http://localhost:8080/rest/api/2/issue/";

        String  payload = new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/resources/CreateBug.json")));

        /**
         * Specify your request --> Say where is payload, where is url, what type of call
         *
         * */
        RequestSpecification requestSpecification = RestAssured.given().body(payload);
        requestSpecification.contentType("application/json");
        requestSpecification.header("Cookie", "JSESSIONID=" + jSessionId);

        Response response = requestSpecification.post(url);
        /*int statusCode = response.statusCode();
        Assert.assertEquals(statusCode, 201);*/
        String stringResponse = response.asString();


        System.out.println(stringResponse);
        JsonPath jsonResponse = new JsonPath(stringResponse);
        String actualKey = jsonResponse.get("key");

        Assert.assertEquals(actualKey, "B1901-51");
        System.out.println("Test Passed");



    }
}
