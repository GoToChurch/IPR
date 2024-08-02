package utils;

import api.dto.response.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.platform.commons.util.ClassLoaderUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CommonUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    public static synchronized <T> List<T> getListFromPageRequest(PageRequest<T> request) {
        Page<T> page =  MAPPER.readValue(
                sendRequest(request),
                MAPPER.getTypeFactory().constructParametricType(Page.class, request.getContentClass())
        );

        return page.getData();
    }

    @SneakyThrows
    private static synchronized <T> String sendRequest(PageRequest<T> request) {
        Map<String, Integer> queryParams = request.asQueryParams();

        return  given()
                .log().parameters()
                .queryParams(queryParams)
                .get(request.endpoint)
                .then()
                .statusCode(200)
                .body("page", equalTo(queryParams.get("page")))
                .body("per_page", equalTo(queryParams.get("per_page")))
                .extract()
                .asString();
    }

    @Nonnull
    public static String getFromResources(String resourcePath) {
        InputStream is = Optional.ofNullable(ClassLoaderUtils.getDefaultClassLoader().getResourceAsStream(resourcePath))
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        try {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed while trying to read data from resource. Resource path: " + resourcePath);
        }
    }
}
