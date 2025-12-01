package br.ifsp.demo.ui.utils.pages.movies;

import br.ifsp.demo.ui.utils.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MovieDetailPage extends BasePage {
    private final By movieTitle = By.tagName("h1");
    private final By genreBadge = By.cssSelector(".badge, .capitalize");
    private final By ratingTitle = By.xpath("//h3[contains(text(), 'Avalie este filme')]");

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
            WebElement form = wait.until(ExpectedConditions.presenceOfElementLocated(ratingTitle));
            return form.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}