package br.ifsp.demo.ui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePageObject {

    private static final String HOME_PATH = "/";

    public HomePage(WebDriver driver) {
        super(driver);
        // Garante que estamos na home (pela URL)
        if (!currentUrl().startsWith("http://localhost:3000" + HOME_PATH)) {
            throw new IllegalStateException("Not on home page. Current url: " + currentUrl());
        }
    }

    public LoginPage clickLogin() {
        // Botão "Login" no header
        driver.findElement(By.xpath("//button[normalize-space()='Login']")).click();
        return new LoginPage(driver);
    }

    public RegisterPage clickRegister() {
        // Botão "Registrar" no header
        driver.findElement(By.xpath("//button[normalize-space()='Registrar']")).click();
        return new RegisterPage(driver);
    }

    public LoginPage clickStartNow() {
        // Botão grande "Comece a Avaliar Agora!"
        driver.findElement(By.xpath("//button[contains(.,'Comece a Avaliar Agora')]")).click();
        return new LoginPage(driver);
    }
}