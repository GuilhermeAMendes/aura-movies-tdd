package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    // ----------- Navegação (tabs da navbar) -----------

    public RecommendationsPage goToRecommendationsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Recomendações"))
        ).click();
        return new RecommendationsPage(driver);
    }

    public CatalogPage goToCatalogTab() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Catálogo"))
        ).click();
        return new CatalogPage(driver);
    }

    // ----------- Verificações -----------

    public boolean isEmptyMessageVisible() {
        // AlertTitle com texto "Nenhuma avaliação encontrada"
        return !driver.findElements(
                By.xpath("//*[contains(text(),'Nenhuma avaliação encontrada')]")
        ).isEmpty();
    }

    public boolean hasRatingForMovie(String movieTitle) {
        // Usa aspas duplas no XPath para permitir títulos com apóstrofo (ex: Pan's Labyrinth)
        String safeTitle = movieTitle.replace("\"", "\\\"");

        String xpath = "//*[contains(normalize-space(),\"" + safeTitle + "\")]";

        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }

    public String getHeaderTitle() {
        // Título "Minhas Avaliações" dentro do card
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Minhas Avaliações')]")
                )
        ).getText();
    }

    // ----------- Remoção de avaliação -----------

    public void removeRatingForMovie(String movieTitle) {
        // Mesmo esquema de escaping para o título
        String safeTitle = movieTitle.replace("\"", "\\\"");

        // 1) Encontra o "card" que contém ao mesmo tempo o título do filme
        //    e um botão de Remover (o span sr-only com texto 'Remover')
        WebElement card = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(
                                "//*[contains(normalize-space(),\"" + safeTitle + "\")]" +
                                        "/ancestor::*[.//button[.//span[normalize-space()='Remover']]][1]"
                        )
                )
        );

        // 2) Dentro do card, encontra o botão de Remover
        WebElement deleteButton = card.findElement(
                By.xpath(".//button[.//span[normalize-space()='Remover']]")
        );

        // 3) Clica para remover
        deleteButton.click();

        // 4) Espera até o título desaparecer da lista
        wait.until(d ->
                d.findElements(
                        By.xpath("//*[contains(normalize-space(),\"" + safeTitle + "\")]")
                ).isEmpty()
        );
    }
}