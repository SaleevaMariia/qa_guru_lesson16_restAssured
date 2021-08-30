
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Locale;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import static org.hamcrest.Matchers.*;

public class ReqresTests {

    @Test
    @DisplayName("Тест GET запроса на поиск списка пользователей")
    void successfullyFindUsers() {
        get("https://reqres.in/api/unknown")
                .then()
                .statusCode(equalTo(200))
                .body("total", greaterThanOrEqualTo(12));
    }

    @Test
    @DisplayName("Тест GET запроса на поиск пользователя с id=5")
    void successfullyFindUser() {
       get("https://reqres.in/api/users/5")
               .then()
               .statusCode(equalTo(200))
               .body("data.id", equalTo(5))
               .body("data.email", notNullValue())
               .body("data.first_name", notNullValue())
               .body("data.last_name", notNullValue())
               .body("data.avatar", equalTo("https://reqres.in/img/faces/5-image.jpg"));
    }

    @Test
    @DisplayName("Негативный тест GET запроса на поиск пользователя с id=0")
    void unsuccessfullyFindUser() {
        get("https://reqres.in/api/users/0")
                .then()
                .statusCode(equalTo(404));
    }

    @Test
    @DisplayName("тест POST запроса на создание пользователя")
    void successfullyCreateUser() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        String job = faker.job().position();
        given()
               .contentType(JSON)
               .body("{\"name\": \"" + name + "\"," +
                "\"job\": \"" + job +"\"}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is(name))
                .body("job", is(job))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    @Test
    @DisplayName("негативный тест POST запроса на создание пользователя")
    void unsuccessfullyCreateUser() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        given()
                .contentType(JSON)
                .body("{\"name\": \"" + name)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(400);
    }
}
