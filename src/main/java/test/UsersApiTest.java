package test;

import api.dto.request.UserCreateAndPatchRequestDto;
import api.dto.response.UserCreateResponseDto;
import api.dto.response.UserPatchResponseDto;
import api.dto.response.UserGetResponseDto;
import config.TestConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import annotations.Api;
import utils.PageRequest;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static utils.CommonUtils.getListFromPageRequest;

@Api
public class UsersApiTest {
    private final String USERS_ENDPOINT = "/users";

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class GetMethodsTests {
        @Test
        public void usersListStatusCodeTest() {
            given()
                    .get(USERS_ENDPOINT)
                    .then()
                    .statusCode(200);
        }

        @Test
        public void usersListStatusContentSizeTest() {
           List<UserGetResponseDto> users = getListFromPageRequest(
                   new PageRequest<UserGetResponseDto>(
                           USERS_ENDPOINT,
                           UserGetResponseDto.class
                   )
           );

            assertEquals(users.isEmpty(), false);
        }

        @Test
        public void usersListPaginationTest() {
            List<UserGetResponseDto> allUsers = getListFromPageRequest(
                    new PageRequest<UserGetResponseDto>(
                            USERS_ENDPOINT,
                            UserGetResponseDto.class
                    )
            );

            List<UserGetResponseDto> usersFirstPage = getListFromPageRequest(
                    new PageRequest<UserGetResponseDto>(
                            USERS_ENDPOINT,
                            UserGetResponseDto.class,
                            0,
                            allUsers.size() / 2
                    )
            );

            assertEquals(allUsers.subList(0, allUsers.size() / 2), usersFirstPage);

            List<UserGetResponseDto> usersSecondPage = getListFromPageRequest(
                    new PageRequest<UserGetResponseDto>(
                            USERS_ENDPOINT,
                            UserGetResponseDto.class,
                            2,
                            allUsers.size() - usersFirstPage.size()
                    )
            );

            assertEquals(allUsers.subList(allUsers.size() - usersFirstPage.size(), allUsers.size()), usersSecondPage);
        }

        @Test
        public void singleUserPositiveStatusCodeTest() {
            UserGetResponseDto user = given()
                    .get(USERS_ENDPOINT + "/2")
                    .then()
                    .log().all()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .getObject("data", UserGetResponseDto.class);

            assertEquals(user.getId(), 2);
        }

        @Test
        public void singleUserNegativeStatusCodeTest() {
            given()
                    .get(USERS_ENDPOINT + "/23")
                    .then()
                    .log().all()
                    .statusCode(404);
        }

        @Test
        public void usersListDelayPositiveTest() {
            given()
                    .queryParam("delay", 3)
                    .get(USERS_ENDPOINT)
                    .then()
                    .log().all()
                    .time(lessThan(Long.parseLong(TestConfig.config.apiTimeout())))
                    .statusCode(200);
        }

        @Test
        public void usersListDelayBrokenTest() {
            given()
                    .queryParam("delay", 6)
                    .get(USERS_ENDPOINT)
                    .then()
                    .log().all()
                    .time(greaterThan(Long.parseLong(TestConfig.config.apiTimeout())))
                    .statusCode(200);
        }
    }

    @Test
    public void userPutTest() {
        UserGetResponseDto userForTest = Optional.ofNullable(getListFromPageRequest(
                new PageRequest<UserGetResponseDto>(
                        USERS_ENDPOINT,
                        UserGetResponseDto.class
                )
        ))
                .get()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No users was found"));

        JSONObject putData = new JSONObject() {{
            put("name", "Andrew");
            put("job", "PE Teacher");
        }};

        UserPatchResponseDto putResponse = given()
                .body(putData.toString())
                .put(USERS_ENDPOINT + String.format("/%s", userForTest.getId()))
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(putData.get("name")))
                .body("job", equalTo(putData.get("job")))
                .extract()
                .as(UserPatchResponseDto.class);

        assertEquals(putResponse.getUpdatedAt().isEmpty(), false);

        LocalDateTime updatedAt = LocalDateTime.parse(putResponse.getUpdatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        LocalDateTime nowDate = LocalDateTime.now(ZoneId.of("UTC"));

        assertEquals(updatedAt.getYear(), nowDate.getYear());
        assertEquals(updatedAt.getMonth(), nowDate.getMonth());
        assertEquals(updatedAt.getDayOfMonth(), nowDate.getDayOfMonth());
        assertEquals(updatedAt.getHour(), nowDate.getHour());
        assertEquals(updatedAt.getMinute(), nowDate.getMinute());
    }

    @Test
    public void userPatchTest() {
        UserGetResponseDto userForTest = Optional.ofNullable(getListFromPageRequest(
                new PageRequest<UserGetResponseDto>(
                        USERS_ENDPOINT,
                        UserGetResponseDto.class
                )
        ))
                .get()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No users was found"));

        HashMap<String, String> patchData = new HashMap<>() {{
            put("name", "Andrew");
            put("job", "PE Teacher");
        }};

        UserPatchResponseDto patchResponse = given()
                .body(patchData)
                .patch(USERS_ENDPOINT + String.format("/%s", userForTest.getId()))
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(patchData.get("name")))
                .body("job", equalTo(patchData.get("job")))
                .extract()
                .as(UserPatchResponseDto.class);

        assertEquals(patchResponse.getUpdatedAt().isEmpty(), false);

        LocalDateTime updatedAt = LocalDateTime.parse(patchResponse.getUpdatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        LocalDateTime nowDate = LocalDateTime.now(ZoneId.of("UTC"));

        assertEquals(updatedAt.getYear(), nowDate.getYear());
        assertEquals(updatedAt.getMonth(), nowDate.getMonth());
        assertEquals(updatedAt.getDayOfMonth(), nowDate.getDayOfMonth());
        assertEquals(updatedAt.getHour(), nowDate.getHour());
        assertEquals(updatedAt.getMinute(), nowDate.getMinute());
    }

    @Test
    public void userPostTest() {
        UserCreateAndPatchRequestDto postData = UserCreateAndPatchRequestDto.builder()
                .job("PE Teacher")
                .name("Andrew")
                .build();

        UserCreateResponseDto postResponse = given()
                .body(postData)
                .post(USERS_ENDPOINT)
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(postData.getName()))
                .body("job", equalTo(postData.getJob()))
                .header("Content-Type", "application/json; charset=utf-8")
                .extract()
                .as(UserCreateResponseDto.class);

        assertEquals(postResponse.getUpdatedAt().isEmpty(), false);

        LocalDateTime updatedAt = LocalDateTime.parse(postResponse.getUpdatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        LocalDateTime nowDate = LocalDateTime.now(ZoneId.of("UTC"));

        assertEquals(updatedAt.getYear(), nowDate.getYear());
        assertEquals(updatedAt.getMonth(), nowDate.getMonth());
        assertEquals(updatedAt.getDayOfMonth(), nowDate.getDayOfMonth());
        assertEquals(updatedAt.getHour(), nowDate.getHour());
        assertEquals(updatedAt.getMinute(), nowDate.getMinute());
    }



    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class DeleteMethodsTests {
        String userToDeleteId;

        @BeforeEach
        void setUp() {
            File postRequestBodyFile = new File("src/main/resources/postRequestBody.json");

            userToDeleteId = given()
                    .body(postRequestBodyFile)
                    .post(USERS_ENDPOINT)
                    .then()
                    .log().body()
                    .statusCode(201)
                    .extract()
                    .jsonPath()
                    .getString("id");
        }

        @Test
        public void userDeleteTest() {
            given()
                    .delete(USERS_ENDPOINT + "/" + userToDeleteId)
                    .then()
                    .statusCode(204);
        }
     }
}
