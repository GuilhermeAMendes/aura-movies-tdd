package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage extends BasePageObject {

    private final WebDriverWait wait;

    public RegisterPage(WebDriver driver) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // Garante que carregou /register
        wait.until(ExpectedConditions.urlContains("/register"));
    }

    public RegisterPage typeName(String name) {
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys(name);
        return this;
    }

    public RegisterPage typeLastName(String lastName) {
        driver.findElement(By.name("lastname")).clear();
        driver.findElement(By.name("lastname")).sendKeys(lastName);
        return this;
    }

    public RegisterPage typeEmail(String email) {
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys(email);
        return this;
    }

    public RegisterPage typePassword(String password) {
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(password);
        return this;
    }

    public LoginPage submitRegister() {
        // Botão "Registrar"
        driver.findElement(By.xpath("//button[contains(text(),'Registrar')]")).click();
        // Depois de registrar ele redireciona para /login
        wait.until(ExpectedConditions.urlContains("/login"));
        return new LoginPage(driver);
    }
}