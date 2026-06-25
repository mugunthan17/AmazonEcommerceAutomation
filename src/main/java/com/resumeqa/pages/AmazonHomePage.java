package com.resumeqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AmazonHomePage extends BasePage {
    private static final By SEARCH_BOX = By.id("twotabsearchtextbox");
    private static final By SEARCH_BUTTON = By.id("nav-search-submit-button");
    private static final By ACCOUNT_LIST = By.id("nav-link-accountList");
    private static final By CART_LINK = By.id("nav-cart");
    private static final By LOGO = By.id("nav-logo-sprites");

    public AmazonHomePage(WebDriver driver) {
        super(driver);
    }

    public AmazonHomePage open(String baseUrl) {
        driver.get(baseUrl);
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(SEARCH_BOX) || isDisplayed(LOGO);
    }

    public SearchResultsPage searchFor(String keyword) {
        type(SEARCH_BOX, keyword);
        click(SEARCH_BUTTON);
        return new SearchResultsPage(driver);
    }

    public LoginPage openSignIn() {
        click(ACCOUNT_LIST);
        return new LoginPage(driver);
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }
}