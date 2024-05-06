package test;
import data.*;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import specification.Specification;
import success.SuccessLogin;
import success.SuccessRegister;
import unsuccess.UnSuccessLogin;
import unsuccess.UnSuccessReg;
import java.util.List;
import static io.restassured.RestAssured.given;

public class ReqresTest {
    private final static String URL ="https://reqres.in/";
    @Test
    public void listUsersTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
        List<UserData> users = given()
                .when()
                .get("api/users?page=2")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x->Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));
    }
    @Test
    public void singleUserTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
        UserData actual = given()
                .when()
                .get("api/users/2")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", UserData.class);
        UserData expected = UserData.builder()
                .id(2)
                .email("janet.weaver@reqres.in")
                .first_name("Janet")
                .last_name("Weaver")
                .avatar("https://reqres.in/img/faces/2-image.jpg")
                .build();
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void userNotFoundTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(404));
        List<UserData> user = given()
                .when()
                .get("api/users/23")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
    }
    @Test
    public void resourceTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
        List<ListResource> resor = given()
                .when()
                .get("api/unknown")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ListResource.class);
        Integer expected = 6;
        Integer actual = resor.size();
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void singleResourceTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
        ListResource actual = given()
                .when()
                .get("api/unknown/2")
                .then().log().all()
                .extract().body().jsonPath().getObject("data", ListResource.class);
        ListResource expected = ListResource.builder()
                .id(2)
                .name("fuchsia rose")
                .year(2001)
                .color("#C74375")
                .pantone_value("17-2031")
                .build();
        Assert.assertEquals(expected,actual);
    }
    @Test
    public void resourceNotFoundTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(404));
        List<ListResource> resor = given()
                .when()
                .get("api/unknown/23")
                .then().log().all()
                .extract().body().jsonPath().getList("data", ListResource.class);
    }
    @Test
    public void createTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(201));
        String name = "morpheus";
        String job = "leader";
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
    @Test
    public void updateTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
        String name = "morpheus";
        String job = "zion resident";
        NewUser newUser = new NewUser(name,job);
        Update update = given()
                .body(newUser)
                .when()
                .put("api/users/2")
                .then().log().all()
                .extract().as(Update.class);
        Assert.assertEquals(name, update.getName());
        Assert.assertEquals(job,update.getJob());
    }
    @Test
    public void deleteUserTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(204));
        given()
                .when()
                .delete(URL+"api/users/2")
                .then().log().all();
    }
    @Test
    public void successRegTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
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
    public void unSuccessRegTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(400));
        Register user = new Register("sydney@fife", "");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);
        Assert.assertEquals("Missing password",unSuccessReg.getError());
    }
    @Test
    public void successLoginTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
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
    public void unSuccessLoginTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(400));
        Login user = new Login("peter@klaven","");
        UnSuccessLogin unSuccessLogin = given()
                .body(user)
                .when()
                .post("/api/login")
                .then().log().all()
                .extract().as(UnSuccessLogin.class);
        Assert.assertEquals("Missing password",unSuccessLogin.getError());
    }
    @Test
    public void delayedUserDataTest(){
        Specification.installSpecification(Specification.RequestSpecification(URL),Specification.responseSpecUnique(200));
        List<UserData> users = given()
                .when()
                .get("api/users?delay=3")
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
        users.forEach(x->Assert.assertTrue(x.getAvatar().contains(x.getId().toString())));
        Assert.assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));
    }
}
