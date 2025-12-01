package br.ifsp.demo.ui.utils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {
    private final By toLoginScreenButton = By.xpath("//button[contains(text(), 'Login')]");
    private final By emailInput = By.xpath("//input[@name='email']");
    private final By passwordInput = By.xpath("//input[@name='password']");
    private final By loginButton = By.xpath("//button[contains(text(), 'Entrar')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getFieldErrorMessage(String inputName) {
        final String xpath = String.format(
                "//input[@name='%s']/following-sibling::p[@data-slot='form-message']",
                inputName
        );

        try {
            WebElement errorElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))
            );
            return errorElement.getText();
        } catch (TimeoutException e) {
            System.out.println("Não encontrou mensagem de erro para o campo: " + inputName);
            throw e;
        }
    }

    public void navigateToLoginPage() {
        click(toLoginScreenButton);
    }

    public void performLogin(String email, String password) {
        navigateToLoginPage();
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
    }

}