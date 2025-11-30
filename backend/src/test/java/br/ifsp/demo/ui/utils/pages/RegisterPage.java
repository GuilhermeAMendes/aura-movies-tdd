package br.ifsp.demo.ui.utils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage extends BasePage {
    private final By toRegisterScreenButton = By.xpath("//button[text()='Registrar']");
    private final By firstNameInput = By.xpath("//input[@name='name']");
    private final By lastNameInput = By.xpath("//input[@name='lastname']");
    private final By emailInput = By.xpath("//input[@name='email']");
    private final By passwordInput = By.xpath("//input[@name='password']");
    private final By registerFormButton = By.xpath("//button[text()='Registrar']");

    public RegisterPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void performRegister(String firstName, String lastName, String email, String password) {
        click(toRegisterScreenButton);
        waitForUrlToBe(FRONT_URL + "register");
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(emailInput, email);
        type(passwordInput, password);
        click(registerFormButton);
    }
}