package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    public HomePage clickLogout() {
        // Procura um botão ou link de logout no header (icon ou texto)
        String logoutXPath =
                "//header//*[self::button or self::a][" +
                        ".//span[contains(normalize-space(),'Sair') or contains(normalize-space(),'Logout')]" +
                        " or .//svg[@data-lucide='log-out' or contains(@class,'lucide-log-out')]" +
                        "]";

        WebElement logoutElement = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath(logoutXPath))
        );

        // Usa JS pra garantir o clique mesmo se tiver tooltip/overlay
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutElement);

        // Após o logout, o app te manda pra HOME ("/"), não para "/login"
        // O próprio construtor de HomePage já valida a URL.
        return new HomePage(driver);
    }

    // ----------- Verificações -----------

    public boolean isRecommendationsTitleVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h1[contains(text(),'Recomendado para Você')]")
        ));
        return true;
    }

    public String getHeaderTitle() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1")
                )
        ).getText();
    }

    public boolean hasAnyRecommendationCard() {
        // cards são <a href="/movies/{id}">
        return !driver.findElements(
                By.cssSelector("a[href^='/movies/']")
        ).isEmpty();
    }

    public String getAlertText() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[role='alert']"))
        ).getText();
    }
}