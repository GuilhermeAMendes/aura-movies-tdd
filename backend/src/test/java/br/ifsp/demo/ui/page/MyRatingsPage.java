package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyRatingsPage extends BasePageObject {

    private final WebDriverWait wait;

    public MyRatingsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // URL do profile: /profile
        wait.until(ExpectedConditions.urlContains("/profile"));
    }

    public boolean isEmptyMessageVisible() {
        // AlertTitle com texto "Nenhuma avaliação encontrada"
        return !driver.findElements(
                By.xpath("//*[contains(text(),'Nenhuma avaliação encontrada')]")
        ).isEmpty();
    }

    public boolean hasRatingForMovie(String movieTitle) {
        // Procura qualquer elemento de texto contendo o título do filme
        return !driver.findElements(
                By.xpath("//*[contains(text(),'" + movieTitle + "')]")
        ).isEmpty();
    }
}