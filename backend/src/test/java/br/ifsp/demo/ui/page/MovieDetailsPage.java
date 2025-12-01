package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MovieDetailsPage extends BasePageObject {

    private final WebDriverWait wait;

    public MovieDetailsPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // espera carregar a rota /movies/{id}
        wait.until(ExpectedConditions.urlContains("/movies/"));
    }

    public MovieDetailsPage selectRating(int value) {
        String text = String.valueOf(value);
        // botão com texto "0", "1", ..., "5"
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='" + text + "']"))
        ).click();
        return this;
    }

    public MovieDetailsPage saveRating() {
        // botão "Salvar" do formulário de avaliação
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Salvar']"))
        ).click();
        return this;
    }

    public int getDisplayedRatingValue() {
        // texto "Sua nota: X"
        String text = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(normalize-space(text()),'Sua nota:')]")
                )
        ).getText(); // ex: "Sua nota: 4"

        String[] parts = text.split(":");
        String numberPart = parts[1].trim(); // "4"
        return Integer.parseInt(numberPart);
    }

    public boolean isRatingFormVisible() {
        // verifica se ainda existe o bloco de botões 0..5 (quando não tem nota)
        return !driver.findElements(
                By.xpath("//button[normalize-space()='0']")
        ).isEmpty();
    }

    public String getMovieTitle() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h1")
                )
        ).getText();
    }

    public MovieDetailsPage clickEditRating() {
        // botão de editar: primeiro botão após o texto "Sua nota:"
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(normalize-space(),'Sua nota:')]/following::button[1]")
        )).click();
        return this;
    }

    public MovieDetailsPage clickDeleteRating() {
        // botão de remover: segundo botão após o texto "Sua nota:"
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(normalize-space(),'Sua nota:')]/following::button[2]")
        )).click();
        return this;
    }
}