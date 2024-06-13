package test;

import data.*;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specification.Specification;
import success.SuccessLogin;
import success.SuccessRegister;
import java.util.List;
import java.util.stream.Stream;

import contants.MyConfig;
import contants.ConstantReader;

import static io.restassured.RestAssured.given;

public class HappyPassTest {
    static MyConfig config;
    @BeforeAll
    public static void getConfigs(){
        config = ConstantReader.reader(MyConfig.class);
        Specification.installSpecification(Specification.requestSpecification(config.url()),Specification.responseSpecUnique(200));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("argus")
    public void listUsers(String name,String param){
        Specification.installSpecification(Specification.requestSpecification(config.url()),Specification.responseSpecUnique(200));
        List<UserData> users = given()
                .when()
                .get(param)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x-> Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));
    }
    static Stream<Object> argus(){
        return Stream.of(
                Arguments.of("listUsers", "api/users?page=2"),
                Arguments.of("delayedUsers", "api/users?delay=3"));
    }

    @Test
    public void singleUser(){
        UserData actual = given()
                .when()
                .get("api/users/2")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", UserData.class);
        UserData expected = UserData.builder()
                .id(2)
                .email("janet.weaver@reqres.in")
                .firstName("Janet")
                .lastName("Weaver")
                .avatar("https://reqres.in/img/faces/2-image.jpg")
                .build();
        Assert.assertEquals(expected,actual);
    }

    @Test
    public void singleResource(){
        Resource actual = given()
                .when()
                .get("api/unknown/2")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", Resource.class);
        Resource expected = Resource.builder()
                .id(2)
                .name("fuchsia rose")
                .year(2001)
                .color("#C74375")
                .pantoneValue("17-2031")
                .build();
        Assert.assertEquals(expected,actual);
    }

    
    @ParameterizedTest
    @MethodSource("arg")
    public void create(String param){
        Specification.installSpecification(Specification.requestSpecification(config.url()),Specification.responseSpecUnique(201));
        String name = "morpheus";
        String job = param;
        NewUser newUser = new NewUser(name,job);
        User user= given()
                .body(newUser)
                .when()
                .post("api/users")
                .then().log().all()
                .extract().as(User.class);
        Assert.assertEquals(name, user.getName());
        Assert.assertEquals(job,user.getJob());
    }
    static Stream<String> arg(){
        return Stream.of("leader","zion resident");
    }
    
    @Test
    public void successReg(){
        Integer id = 4;
        String token = "QpwL5tke4Pnpja7X4";
        Register user = new Register("eve.holt@reqres.in", "pistol");
        SuccessRegister successRegister = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(SuccessRegister.class);
        Assert.assertEquals(id,successRegister.getId());
        Assert.assertEquals(token,successRegister.getToken());
    }

    @Test
    public void successLogin(){
        String token = "QpwL5tke4Pnpja7X4";
        Login user = new Login("eve.holt@reqres.in", "cityslicka");
        SuccessLogin successLogin = given()
                .body(user)
                .when()
                .post("api/login")
                .then().log().all()
                .extract().as(SuccessLogin.class);
        Assert.assertEquals(token,successLogin.getToken());
    }

    @Test
    public void resource(){
        List<Resource> resor = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", Resource.class);
        Integer expected = 6;
        Integer actual = resor.size();
        Assert.assertEquals(expected,actual);
    }
    
    @Test
    public void deleteUser(){
        Specification.installSpecification(Specification.requestSpecification(config.url()),Specification.responseSpecUnique(204));
        given()
                .when()
                .delete(config.url()+"api/users/2")
                .then().log().all();
    }
}
