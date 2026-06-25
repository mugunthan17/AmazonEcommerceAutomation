package com.resumeqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {
    private static final By EMAIL_INPUT = By.id("ap_email");
    private static final By PASSWORD_INPUT = By.id("ap_password");
    private static final By SIGN_IN_HEADING = By.cssSelector("h1.a-spacing-small");
    private static final By CHECKOUT_PAGE = By.cssSelector("#checkoutDisplayPage, #spc-orders, input[name='placeYourOrder1']");
    private static final By PLACE_ORDER = By.name("placeYourOrder1");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isSignInGateDisplayed() || isCheckoutPageDisplayed() || isRobotCheckDisplayed();
    }

    public boolean isSignInGateDisplayed() {
        return isDisplayed(EMAIL_INPUT) || isDisplayed(PASSWORD_INPUT);
    }

    public boolean isCheckoutPageDisplayed() {
        return isDisplayed(CHECKOUT_PAGE);
    }

    public boolean isPlaceOrderButtonDisplayed() {
        return isDisplayed(PLACE_ORDER);
    }

    public String getPageMessage() {
        if (isDisplayed(SIGN_IN_HEADING)) {
            return textOf(SIGN_IN_HEADING);
        }
        return driver.getTitle();
    }
}