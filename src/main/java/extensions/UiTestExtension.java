package extensions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import config.TestConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.clearBrowserLocalStorage;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.clearBrowserCache;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class UiTestExtension implements BeforeAllCallback, AfterEachCallback {
    private static boolean started = false;

    @SneakyThrows
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        synchronized (ApiTestExtension.class) {
            if (!started) {
                started = true;
                ChromeOptions options = new ChromeOptions();
                if (TestConfig.config.selenoidUrl() != null && !TestConfig.config.selenoidUrl().isEmpty()) {
                    Map<String, Object> optionsForSelenoid = new HashMap<>();
                    optionsForSelenoid.put("enableVNC", TestConfig.config.enableVNC());
                    optionsForSelenoid.put("enableVideo", TestConfig.config.enableVideo());
                    optionsForSelenoid.put("sessionTimeout", "20m");

                    options.setCapability("selenoid:options", optionsForSelenoid);

                    Configuration.remote = TestConfig.config.selenoidUrl();
                }

                options.setCapability("chrome:options", new HashMap<String, Object>() {{
                    put("env", List.of("LANG=en_US.UTF-8", "LANGUAGE=en:US", "LC_ALL=en_US.UTF-8"));
                }});

                options.addArguments("--start-incognito");

                Configuration.browserCapabilities = options;
                Configuration.browserSize = TestConfig.config.browserSize();
                Configuration.browser = TestConfig.config.browser();
                Configuration.timeout = TestConfig.config.timeout();
                Configuration.baseUrl = TestConfig.config.url();
                Configuration.savePageSource = true;
                Configuration.headless = TestConfig.config.headless();
                Configuration.proxyEnabled = false;
            }
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        getWebDriver().manage().deleteAllCookies();
        clearBrowserLocalStorage();
        clearBrowserCache();
        int windows = WebDriverRunner.getWebDriver().getWindowHandles().size();
        for (int i = 0; i < windows - 1; i++) {
            switchTo().window(1).close();
        }
        switchTo().window(0);
    }
}
