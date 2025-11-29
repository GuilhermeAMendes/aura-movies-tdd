package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RecommendationsPage extends BasePageObject {

    private final WebDriverWait wait;

    public RecommendationsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/recommendations"));
    }

    // ----------- Navegação -----------

    public RecommendationsPage goToRecommendationsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Recomendações")))
                .click();
        return this;
    }

    public CatalogPage goToCatalogTab() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Catálogo")))
                .click();
        return new CatalogPage(driver);
    }

    public MyRatingsPage goToMyRatingsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Minhas Avaliações")))
                .click();
        return new MyRatingsPage(driver);
    }

    // ----------- Verificações -----------

    public boolean isRecommendationsTitleVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Recomendado para Você')]")
        ));
        return true; // se não explodiu, está visível
    }

    public String getHeaderTitle() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1")
                )
        ).getText();
    }

    /**
     * Verifica se existe ao menos um card de filme.
     * Os movie cards são links para "/movies/{UUID}"
     */
    public boolean hasAnyRecommendationCard() {
        return !driver.findElements(
                By.cssSelector("a[href^='/movies/']")
        ).isEmpty();
    }
}