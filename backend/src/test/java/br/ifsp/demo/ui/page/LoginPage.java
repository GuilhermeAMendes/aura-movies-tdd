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

    public RecommendationsPage submitValidLogin() {
        driver.findElement(By.xpath("//button[contains(text(),'Entrar')]")).click();
        wait.until(ExpectedConditions.urlContains("/recommendations"));
        return new RecommendationsPage(driver);
    }

    public LoginPage submitInvalidLogin() {
        driver.findElement(By.xpath("//button[contains(text(),'Entrar')]")).click();
        // aqui você pode esperar aparecer o alerta de erro
        return this;
    }

    // Exemplo para futuro: pegar mensagem de erro (quando você descobrir o seletor)
    public String getErrorMessage() {
        // Ajustar o seletor conforme o componente de alerta (shadcn alert?)
        return driver.findElement(By.cssSelector("[role='alert']")).getText();
    }
}