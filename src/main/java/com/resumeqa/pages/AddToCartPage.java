package com.resumeqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddToCartPage extends BasePage {
    private static final By SUCCESS_MESSAGE = By.cssSelector("#NATC_SMART_WAGON_CONF_MSG_SUCCESS, #huc-v2-order-row-confirm-text, .a-alert-success .a-alert-heading");
    private static final By VIEW_CART_BUTTON = By.cssSelector("#sw-gtc a, #attach-sidesheet-view-cart-button, a[href*='/cart']");
    private static final By CART_LINK = By.id("nav-cart");
    private static final By CART_COUNT = By.id("nav-cart-count");
    private static final By CLOSE_SIDE_SHEET = By.id("attach-close_sideSheet-link");

    public AddToCartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(SUCCESS_MESSAGE) || isDisplayed(VIEW_CART_BUTTON) || isDisplayed(CART_LINK);
    }

    public boolean isSuccessMessageDisplayed() {
        return isDisplayed(SUCCESS_MESSAGE) || isDisplayed(VIEW_CART_BUTTON);
    }

    public int getHeaderCartCount() {
        if (!isDisplayed(CART_COUNT)) {
            return 0;
        }
        String text = textOf(CART_COUNT).replaceAll("[^0-9]", "");
        return text.isBlank() ? 0 : Integer.parseInt(text);
    }

    public CartPage openCart() {
        if (isDisplayed(VIEW_CART_BUTTON)) {
            click(VIEW_CART_BUTTON);
        } else {
            if (isDisplayed(CLOSE_SIDE_SHEET)) {
                click(CLOSE_SIDE_SHEET);
            }
            click(CART_LINK);
        }
        return new CartPage(driver);
    }
}