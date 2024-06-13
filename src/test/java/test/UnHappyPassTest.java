package test;

import contants.ConstantReader;
import contants.MyConfig;
import data.*;
import junit.framework.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import specification.Specification;
import unsuccess.UnSuccessReg;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class UnHappyPassTest {
    static MyConfig config;
    @BeforeAll
    public static void getConfigs(){
        config = ConstantReader.reader(MyConfig.class);
        Specification.installSpecification(Specification.requestSpecification(config.url()),Specification.responseSpecUnique(400));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("argum")
    public void NotFound(String name, String param){
        Specification.installSpecification(Specification.requestSpecification(config.url()),Specification.responseSpecUnique(404));
        List<UserData> user = given()
                .when()
                .get(param)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserData.class);
    }
    static Stream<Object> argum(){
        return Stream.of(
                    Arguments.of("userNotFound", "api/users/23"),
                    Arguments.of("resourceNotFound", "api/unknown/23"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("argumen")
    public void unSuccess(String name, String param){
        Register user = new Register("sydney@fife", "");
        UnSuccessReg unSuccessReg = given()
                .body(user)
                .when()
                .post("api/register")
                .then().log().all()
                .extract().as(UnSuccessReg.class);
        Assert.assertEquals("Missing password",unSuccessReg.getError());
    }
    static Stream<Object> argumen(){
        return Stream.of(
                Arguments.of("unSuccessReg", "api/register"),
                Arguments.of("unSuccessLogin", "api/login"));
    }



}
