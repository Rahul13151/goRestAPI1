package api.endpoints;
import api.payload.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class UserEndPoints {
    public static Response createUser(User payload,String token) throws JsonProcessingException {

        // Using POJO(Plain Old Java Object Method to pass payload
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(payload);

        Response response = given()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Authorization","Bearer "+ token)
                .body(json)
           .when()
                .post(Routes.post_url);
        return response;
    }
    public static Response getUser(String userId,String token){
        Response response = given()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Authorization","Bearer " + token )
                .pathParam("id",userId)
            .when()
                .get(Routes.get_singleUser_url);
        return response;
    }
    public static Response getAllUsers(String token){
        Response response = given()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Authorization","Bearer "+token)
                .when()
                .get(Routes.get_allUsers_url);
        return response;
    }
    public static Response updateUser(String userId, User payload,String token){
        System.out.println();
        Response response = given()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Authorization","Bearer "+token)
                .pathParam("id",userId)
                .body(payload)
            .when()
                .patch(Routes.update_url);
        return response;
    }
    public static Response deleteUser(String userId,String token){
        Response response = given()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Authorization","Bearer "+token)
                .pathParam("id",userId)
            .when()
                .delete(Routes.delete_url);
        return response;
    }


}
