package br.ifsp.demo.ui.utils.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;

public class MyRatingsPage extends BasePage {
    private final By myRatingsLink = By.xpath("//a[contains(@href, '/profile')]");
    private final By pageTitle = By.xpath("//*[contains(text(), 'Minhas Avaliações')]");
    private final By emptyStateAlert = By.xpath("//div[contains(text(), 'Nenhuma avaliação encontrada')]");

    public MyRatingsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void navigateToMyRatings() {
        if (!Objects.requireNonNull(driver.getCurrentUrl()).contains("/profile")) {
            try {
                click(myRatingsLink);
            } catch (Exception e) {
                driver.get(FRONT_URL + "profile");
            }
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    public int getRatingForMovie(String movieTitle) {
        if (isListEmpty()) {
            throw new RuntimeException("FALHA: A lista de avaliações está vazia! O filme '" + movieTitle + "' não foi salvo.");
        }

        String ratingXpath = String.format(
                "//div[contains(@class, 'cursor-pointer')][.//p[contains(., \"%s\")]]//span[contains(@class, 'font-bold')]",
                movieTitle.trim()
        );

        try {
            WebElement ratingElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ratingXpath)));
            String gradeText = ratingElement.getText();
            return Integer.parseInt(gradeText.trim());
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public void clickMovieToEdit(String movieTitle) {
        if (isListEmpty()) {
            throw new RuntimeException("Não é possível editar: A lista de avaliações está vazia.");
        }

        String movieRowXpath = String.format("//div[contains(@class, 'cursor-pointer')][.//p[contains(., \"%s\")]]", movieTitle.trim());

        try {
            WebElement rowElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(movieRowXpath)));
            rowElement.click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Não foi possível clicar no filme para editar: " + movieTitle, e);
        }
    }

    private boolean isListEmpty() {
        try {
            new WebDriverWait(driver, java.time.Duration.ofMillis(500))
                    .until(ExpectedConditions.visibilityOfElementLocated(emptyStateAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}