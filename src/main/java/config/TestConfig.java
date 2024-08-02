package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:properties", "system:env", "file:src/main/resources/baseTestConfig.properties"})
public interface TestConfig extends Config {
    TestConfig config = ConfigFactory.create(TestConfig.class);

    String url();

    long timeout();
    String browserSize();
    String browser();

    @Key("selenoid.url")
    String selenoidUrl();

    @DefaultValue("false")
    boolean headless();

    @Key("api.baseURI")
    String baseURI();

    @Key("api.timeout")
    String apiTimeout();

    @DefaultValue("true")
    boolean enableVNC();

    @DefaultValue("false")
    boolean enableVideo();
}