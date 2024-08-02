package extensions;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.DecoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class ApiTestExtension implements BeforeAllCallback {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        synchronized (ApiTestExtension.class) {
            if (!started) {
                started = true;
                RestAssured.baseURI = TestConfig.config.baseURI();
                RestAssured.config = RestAssured.config()
                        .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2))
                        .httpClient(HttpClientConfig.httpClientConfig()
                                        .setParam("http.connection.timeout", TestConfig.config.apiTimeout())
                                        .setParam("http.socket.timeout", TestConfig.config.apiTimeout())
                        )
                        .decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8"));
                RestAssured.requestSpecification = new RequestSpecBuilder()
                        .setBaseUri(RestAssured.baseURI)
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .build();
                extensionContext.getRoot().getStore(GLOBAL).put("beforehookApi", this);
            }
        }
    }
}
