package com.resumeqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {
    private static final By CART_FORM = By.id("activeCartViewForm");
    private static final By CART_ITEMS = By.cssSelector(".sc-list-item-content, div[data-name='Active Items'] .sc-list-item");
    private static final By SUBTOTAL = By.cssSelector("#sc-subtotal-amount-activecart .sc-price, #sc-subtotal-amount-activecart .a-size-medium, .sc-price");
    private static final By PROCEED_TO_CHECKOUT = By.name("proceedToRetailCheckout");
    private static final By DELETE_BUTTON = By.cssSelector("input[value='Delete'], input[data-action='delete']");
    private static final By EMPTY_CART_TEXT = By.cssSelector("h1, .sc-your-amazon-cart-is-empty");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(CART_FORM) || isDisplayed(EMPTY_CART_TEXT) || isRobotCheckDisplayed();
    }

    public int getCartItemCount() {
        return count(CART_ITEMS);
    }

    public String getSubtotalText() {
        return isDisplayed(SUBTOTAL) ? textOf(SUBTOTAL) : "";
    }

    public boolean hasCheckoutButton() {
        return isDisplayed(PROCEED_TO_CHECKOUT);
    }

    public CheckoutPage proceedToCheckout() {
        click(PROCEED_TO_CHECKOUT);
        return new CheckoutPage(driver);
    }

    public CartPage removeFirstItemIfPresent() {
        if (isDisplayed(DELETE_BUTTON)) {
            click(DELETE_BUTTON);
        }
        return this;
    }
}