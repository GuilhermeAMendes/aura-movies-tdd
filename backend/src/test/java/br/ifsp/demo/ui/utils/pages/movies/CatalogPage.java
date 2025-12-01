package br.ifsp.demo.ui.utils.pages.movies;

import br.ifsp.demo.ui.utils.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CatalogPage extends BasePage {
    private final By pageTitle = By.xpath("//h1[contains(text(), 'Catálogo Completo')]");
    private final By movieCards = By.xpath("//div[contains(@class, 'grid')]//a");
    private final By toMoviesLink = By.xpath("//a[contains(@href, '/movies')]");
    private final By cardTitle = By.xpath(".//*[contains(@class, 'font-bold')]");

    public CatalogPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void navigateToCatalog() {
        click(toMoviesLink);
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
    }

    public boolean isCatalogLoaded() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
            return title.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getMoviesCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[href^='/movies/'], [role='alert']")));
            List<WebElement> movies = driver.findElements(movieCards);

            return movies.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickFirstMovie() {
        clickMovieByIndex(0);
    }

    public void clickMovieByIndex(int index) {
        List<WebElement> movies = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(movieCards));
        if (movies.size() > index) {
            WebElement movieLink = movies.get(index);
            wait.until(ExpectedConditions.elementToBeClickable(movieLink));
            movieLink.click();
        } else {
            throw new IndexOutOfBoundsException("Não foi possível clicar no filme de índice " + index + ". Total encontrado: " + movies.size());
        }
    }

    public String getMovieTitleByIndex(int index) {
        List<WebElement> movies = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(movieCards));
        if (movies.size() > index) {
            WebElement movieLink = movies.get(index);
            WebElement titleElement = movieLink.findElement(cardTitle);
            return titleElement.getText();
        }
        return "";
    }
}