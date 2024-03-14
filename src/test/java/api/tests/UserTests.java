package api.tests;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utils.Config;
import api.utils.RandomGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static api.utils.Config.invalidToken;
import static api.utils.Config.token;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserTests {

    User userPayload;

    @BeforeClass
    public void setupData(){
        RestAssured.baseURI = Config.base_url;
        RandomGenerator.generateRandomVariables();
        userPayload = RandomGenerator.generateUser();
    }

    @Test(priority = 1)
    @Description("Create User(TC-01)[P] User Should Be Created With Valid Inputs")
    public void createUserWithValidInputs() throws JsonProcessingException{

        Response response = UserEndPoints.createUser(userPayload,token);
        Config.id = (response.jsonPath().getString("id"));
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),201,"Status code is 201-Created");
        //Asserting the response header
        Assert.assertEquals(response.header("Content-Type"),"application/json; charset=utf-8");
        Assert.assertEquals(response.jsonPath().getString("email"),userPayload.getEmail());
    }

    @Test(priority = 2)
    @Description("Create User(TC-02)[N] User Should Not Be Created With Invalid Email")
    public void createUserWithInvalidMail() throws JsonProcessingException{
        userPayload.setEmail(Config.invalidEmail);
        Response response = UserEndPoints.createUser(userPayload,token);
        Assert.assertEquals(response.getStatusCode(),422,"Status code is 422 - Unprocessable Entity");
        // Asserting if Relevant error message is returned
        assertThat(response.jsonPath().getList("$"), hasItem(hasEntry("field", "email")));
        assertThat(response.jsonPath().getList("$"), hasItem(hasEntry("message", "is invalid")));
    }

    @Test(priority = 3)
    @Description("Create User(TC-03)[N] User Should Not Be Created if an user pre-exist with the same email")
    public void createUserWithPreExistEmail() throws JsonProcessingException{
        userPayload.setEmail(Config.email);
        Response response = UserEndPoints.createUser(userPayload,token);
        Assert.assertEquals(response.getStatusCode(),422,"Status code is 422 - Unprocessable Entity");
        // Asserting if Relevant error message is returned
        assertThat(response.jsonPath().getList("$"), hasItem(hasEntry("field", "email")));
        assertThat(response.jsonPath().getList("$"), hasItem(hasEntry("message", "has already been taken")));
    }
    @Test(priority = 4)
    @Description("Create User(TC-04)[P] User can be created with pre-existing name,gender and status")
    public void createUserWithPreExistPayloadAndNewMail() throws JsonProcessingException{
        RandomGenerator.generateRandomMail();
        userPayload.setEmail(Config.email);
        Response response = UserEndPoints.createUser(userPayload,token);
        Assert.assertEquals(response.getStatusCode(),201,"Status code is 201-Created");
        Assert.assertEquals(response.jsonPath().getString("name"),userPayload.getName());
    }
    @Test(priority = 5)
    @Description("Create User(TC-05)[N] User Should Not Be Created with Invalid Token/Authentication")
    public void createUserWithInvalidToken() throws JsonProcessingException{
        Response response = UserEndPoints.createUser(userPayload,invalidToken);
        Assert.assertEquals(response.getStatusCode(),401,"Status code is 401 - Unauthorized");
    }

    @Test(priority = 6)
    @Description("Update User(TC-06)[P] Update all Fields with Valid User Credentials")
    public void updateFieldsWithValidInput() throws JsonProcessingException{
        RandomGenerator.generateRandomMail();
        RandomGenerator.generateRandomName();
        RandomGenerator.generateRandomGender();
        userPayload = RandomGenerator.generateUser();
        Response response = UserEndPoints.updateUser(Config.id,userPayload,token);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200,"Status code is 200");
        Assert.assertEquals(response.jsonPath().getString("email"),userPayload.getEmail());
        Assert.assertEquals(response.jsonPath().getString("name"),userPayload.getName());
    }
    @Test(priority = 7)
    @Description("Update User(TC-07)[N] Relevant error message shown when an invalid user id is passed")
    public void updateFieldsWithInvalidId() throws JsonProcessingException{
        Response response = UserEndPoints.updateUser(Config.invalidId,userPayload,token);
        Assert.assertEquals(response.getStatusCode(),404,"Status code is 404-Not Found");
        //Asserting relevant error message shown
        Assert.assertEquals(response.jsonPath().getString("message"),"Resource not found");
    }
    @Test(priority = 8)
    @Description("Update User(TC-08)[N] User should not be Updated when an invalid Authorization Token is passed")
    public void updateFieldsWithInvalidToken() throws JsonProcessingException{
        Response response = UserEndPoints.updateUser(Config.invalidId,userPayload,invalidToken);
        Assert.assertEquals(response.getStatusCode(),401,"Status code is 401-Unauthorized");
    }
    @Test(priority = 9)
    @Description("Update User(TC-09)[N] Update must not be made with Invalid Patch Data request")
    public void updateFieldsWithInvalidData() throws JsonProcessingException{
        userPayload.setGender("third");
        Response response = UserEndPoints.updateUser(Config.id,userPayload,token);
        Assert.assertEquals(response.getStatusCode(),422,"Status code is 422 Unprocessable Entity");
    }

    @Test(priority = 10)
    @Description("Get User(TC-10)[P] All Users should Be retrieved with valid URL")
    public void getAllUserWithValidURL(){
        Response response = UserEndPoints.getAllUsers(token);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200,"Status Code is 200");
    }

    @Test(priority = 11)
    @Description("Get User(TC-11)[N] Users should Not Be retrieved with Invalid Access Token")
    public void getAllUserWithInvalidCredential(){
        Response response = UserEndPoints.getAllUsers(invalidToken);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),401,"Status Code is 401-Unauthorized");
    }
    @Test(priority = 12)
    @Description("Get User(TC-12)[P] User should be retrieved with a valid user Id")
    public void getExistingUser(){
        Response response = UserEndPoints.getUser(Config.id,token);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),200,"Status Code is 200");
        Assert.assertEquals(response.jsonPath().getString("id"),Config.id);
        Assert.assertEquals(response.jsonPath().getString("email"),userPayload.getEmail());
    }
    @Test(priority = 13)
    @Description("Get User(TC-13)[N] User should Not be retrieved with a Invalid user Id")
    public void getANonExistingUser(){
        Response response = UserEndPoints.getUser(Config.invalidId,token);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),404,"Status Code is 404-Not Found");
    }
    @Test(priority = 14)
    @Description("Delete User(TC-14)[P] User Should be Deleted When a pre-existing User Id is passed")
    public void deleteExistingUser(){
        Response response = UserEndPoints.deleteUser(Config.id,token);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),204,"Status code is 204");
        response = UserEndPoints.getUser(Config.id,token);
        Assert.assertEquals(response.getStatusCode(),404);
    }
    @Test(priority = 15)
    @Description("Delete User(TC-15)[N] Relevant Error message shown with Invalid User Id")
    public void deleteANonExistingUser(){
        Response response = UserEndPoints.deleteUser(Config.invalidId,token);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),404,"Status code is 404 - Not Found");
    }
    @Test(priority = 16)
    @Description("Delete User(TC-16)[N] User Should Not be Deleted with an Invalid Authorization Token")
    public void deleteUserWithInvalidToken(){
        Response response = UserEndPoints.deleteUser(Config.id,invalidToken);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),401,"Status Code is 401-Unauthorized");
    }
    @Test(priority = 17)
    @Description("Delete User(TC-17)[N] User Should Not be Deleted with an Empty Authorization Token")
    public void deleteUserWithEmptyToken(){
        Response response = UserEndPoints.deleteUser(Config.id,Config.emptyString);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),404,"Not Found");
    }
    @Test(priority = 18)
    @Description("Delete User(TC-18)[N] Error message shown with Empty User Id")
    public void deleteUserRequestWithEmptyId(){
        Response response = UserEndPoints.deleteUser(Config.emptyString,token);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(),404,"Status code is 404 - Not Found");
    }

}
