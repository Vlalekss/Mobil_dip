package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import drivers.BrowserstackDriver;
import drivers.EmulatorDriver;
import helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.*;
public class TestBase {
    public static String env = System.getProperty("env");
    @BeforeAll
    static void beforeAll() {
        if (env == null) {
            env = "emulator";
        }
        switch (System.getProperty("env")) {
            case "browserstack":
                Configuration.browser = BrowserstackDriver.class.getName();
                break;
            case "emulator":
                Configuration.browser = EmulatorDriver.class.getName();
                break;

        }
        Configuration.browserSize = null;
        Configuration.timeout = 15000;
        Configuration.pageLoadTimeout = 15000;
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        open();
    }

    @AfterEach
    void addAttachments() {
        String sessionId = sessionId().toString();
        Attach.pageSource();
        closeWebDriver();
        if (!System.getProperty("env").equals("emulator")) Attach.addVideo(sessionId);
    }
}

