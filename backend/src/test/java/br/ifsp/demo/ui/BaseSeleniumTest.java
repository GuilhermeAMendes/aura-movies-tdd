package br.ifsp.demo.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public abstract class BaseSeleniumTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "http://localhost:3000";

    @BeforeEach
    void setUp() {
        // Configura o driver automaticamente
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Pequena espera implícita global
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        // Hook para abrir a página inicial do teste
        setInitialPage();
    }

    /**
     * Hook que as subclasses podem sobrescrever para definir a página inicial.
     */
    protected void setInitialPage() {
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}