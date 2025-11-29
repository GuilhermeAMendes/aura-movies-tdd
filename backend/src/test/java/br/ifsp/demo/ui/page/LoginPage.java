package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePageObject {

    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/login"));
    }

    // ----------- Preenchimento de campos -----------

    public LoginPage typeEmail(String email) {
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys(email);
        return this;
    }

    public LoginPage typePassword(String password) {
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        return this;
    }

    // ----------- Submissão -----------

    /**
     * Fluxo feliz: credenciais válidas, redireciona para /recommendations.
     */
    public RecommendationsPage submitValidLogin() {
        driver.findElement(By.xpath("//button[contains(text(),'Entrar')]")).click();
        wait.until(ExpectedConditions.urlContains("/recommendations"));
        return new RecommendationsPage(driver);
    }

    /**
     * Fluxo inválido: credenciais erradas, permanece na tela de login
     * e exibe um toast "Falha no Login".
     */
    public LoginPage submitWithInvalidCredentials() {
        driver.findElement(By.xpath("//button[contains(text(),'Entrar')]")).click();
        // Não há redirect; o teste deve checar o toast de erro.
        return this;
    }

    // ----------- Mensagens de validação / erro -----------

    public boolean isEmailValidationMessageVisible() {
        return !driver.findElements(
                By.xpath("//*[contains(normalize-space(),'Por favor, insira um e-mail válido.')]")
        ).isEmpty();
    }

    public boolean isPasswordRequiredMessageVisible() {
        return !driver.findElements(
                By.xpath("//*[contains(normalize-space(),'A senha é obrigatória.')]")
        ).isEmpty();
    }

    public boolean isLoginErrorToastVisible() {
        return !driver.findElements(
                By.xpath("//*[contains(normalize-space(),'Falha no Login')]")
        ).isEmpty();
    }
}