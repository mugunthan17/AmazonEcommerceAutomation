package com.resumeqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {
    private static final By PRODUCT_TITLE = By.id("productTitle");
    private static final By PRICE = By.cssSelector("#corePriceDisplay_desktop_feature_div .a-price .a-offscreen, #priceblock_ourprice, #priceblock_dealprice, .a-price .a-offscreen");
    private static final By ADD_TO_CART_BUTTON = By.id("add-to-cart-button");
    private static final By BUY_NOW_BUTTON = By.id("buy-now-button");
    private static final By CART_CONFIRMATION = By.cssSelector("#NATC_SMART_WAGON_CONF_MSG_SUCCESS, #huc-v2-order-row-confirm-text, .a-alert-success .a-alert-heading");
    private static final By VIEW_CART_BUTTON = By.cssSelector("#sw-gtc a, #attach-sidesheet-view-cart-button, a[href*='/cart']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(PRODUCT_TITLE) || isRobotCheckDisplayed();
    }

    public String getProductTitle() {
        return isDisplayed(PRODUCT_TITLE) ? textOf(PRODUCT_TITLE) : driver.getTitle();
    }

    public String getPriceText() {
        return isDisplayed(PRICE) ? textOf(PRICE) : "";
    }

    public boolean canAddToCart() {
        return isDisplayed(ADD_TO_CART_BUTTON);
    }

    public boolean canBuyNow() {
        return isDisplayed(BUY_NOW_BUTTON);
    }

    public AddToCartPage addToCart() {
        click(ADD_TO_CART_BUTTON);
        return new AddToCartPage(driver);
    }

    public CheckoutPage buyNow() {
        click(BUY_NOW_BUTTON);
        return new CheckoutPage(driver);
    }

    public boolean isAddToCartConfirmed() {
        return isDisplayed(CART_CONFIRMATION) || isDisplayed(VIEW_CART_BUTTON);
    }
}