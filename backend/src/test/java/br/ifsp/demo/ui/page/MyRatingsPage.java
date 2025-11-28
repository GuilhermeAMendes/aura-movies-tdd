package br.ifsp.demo.ui.page;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class MyRatingsPage extends BasePageObject {

    private final WebDriverWait wait;

    public MyRatingsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // URL do profile: /profile
        wait.until(ExpectedConditions.urlContains("/profile"));
    }

    // Depois vou colocar métodos para ler/remover avaliações.
}