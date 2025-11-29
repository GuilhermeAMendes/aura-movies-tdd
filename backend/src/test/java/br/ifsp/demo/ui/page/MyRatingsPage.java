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
        // antes: esperava urlContains("/profile"), o que estava gerando timeout
        // agora deixamos para os métodos de verificação (como getHeaderTitle) fazerem os waits necessários
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
        // Procura qualquer elemento de texto contendo o título do filme
        return !driver.findElements(
                By.xpath("//*[contains(text(),'" + movieTitle + "')]")
        ).isEmpty();
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
        // 1) encontra o elemento que contém o título do filme
        WebElement titleElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(normalize-space(),'" + movieTitle + "')]")
                )
        );

        // 2) sobe até um ancestral que tenha um botão com ícone de lixeira (é o card dessa avaliação)
        WebElement card = titleElement.findElement(
                By.xpath("./ancestor::*[.//button[.//svg[contains(@class,'lucide-trash')]]][1]")
        );

        // 3) dentro do card, acha o botão de remover
        WebElement deleteButton = card.findElement(
                By.xpath(".//button[.//svg[contains(@class,'lucide-trash')]]")
        );

        // 4) clica para remover
        deleteButton.click();

        // 5) espera o título sumir da página
        wait.until(driver ->
                driver.findElements(
                        By.xpath("//*[contains(normalize-space(),'" + movieTitle + "')]")
                ).isEmpty()
        );
    }
}