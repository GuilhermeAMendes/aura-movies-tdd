package br.ifsp.demo.ui.utils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
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