package br.ifsp.demo.ui.utils.pages.movies;

import br.ifsp.demo.ui.utils.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MovieDetailPage extends BasePage {
    private final By movieTitle = By.tagName("h1");
    private final By genreBadge = By.cssSelector(".badge, .capitalize");
    private final By saveButton = By.xpath("//button[contains(., 'Salvar')]");
    private final By editButton = By.xpath("//button[.//svg[contains(@class, 'lucide-pencil')]]");

    public MovieDetailPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public String getMovieTitle() {
        WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(movieTitle));
        return titleElement.getText();
    }

    public String getMovieGenre() {
        WebElement badgeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(genreBadge));
        return badgeElement.getText();
    }

    public boolean isRatingFormVisible() {
        try {
            return driver.findElement(saveButton).isDisplayed() ||
                    driver.findElement(editButton).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public void rateMovie(int starValue) {
        if (starValue < 0 || starValue > 5) {
            throw new IllegalArgumentException("A nota deve ser entre 0 e 5");
        }

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement editBtn = shortWait.until(ExpectedConditions.visibilityOfElementLocated(editButton));
            editBtn.click();

            Thread.sleep(200);
        } catch (Exception ignored) {}

        wait.until(ExpectedConditions.visibilityOfElementLocated(saveButton));

        By ratingButtonLocator = By.xpath(String.format(
                "//button[normalize-space()='%d' and contains(@class, 'rounded-md') and not(contains(., 'Salvar'))]",
                starValue
        ));

        WebElement ratingButton = wait.until(ExpectedConditions.elementToBeClickable(ratingButtonLocator));
        ratingButton.click();

        try { Thread.sleep(200); } catch (InterruptedException e) {}

        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(saveButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}
    }
}