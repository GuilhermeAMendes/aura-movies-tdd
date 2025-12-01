package br.ifsp.demo.ui.page;

import org.openqa.selenium.WebDriver;

public abstract class BasePageObject {

    protected final WebDriver driver;

    protected BasePageObject(WebDriver driver) {
        this.driver = driver;
    }

    public String pageTitle() {
        return driver.getTitle();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}