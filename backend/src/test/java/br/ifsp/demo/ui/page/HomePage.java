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
        driver.findElement(By.linkText("Login")).click();
        return new LoginPage(driver);
    }

    public RegisterPage clickRegister() {
        driver.findElement(By.linkText("Registrar")).click();
        return new RegisterPage(driver);
    }

    public LoginPage clickStartNow() {
        // Botão "Comece a Avaliar Agora!"
        driver.findElement(By.xpath("//*[contains(text(),'Comece a Avaliar Agora')]")).click();
        return new LoginPage(driver);
    }
}