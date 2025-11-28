package br.ifsp.demo.ui.page;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class CatalogPage extends BasePageObject {

    private final WebDriverWait wait;

    public CatalogPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // URL do catálogo: /movies
        wait.until(ExpectedConditions.urlContains("/movies"));
    }

    // Depois vou adicionar métodos como:
    // - listar cards
    // - abrir um filme específico etc.
}