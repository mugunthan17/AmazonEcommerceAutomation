package com.resumeqa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private static final By EMAIL_INPUT = By.xpath("//input[@id='ap_email_login']");
    private static final By CONTINUE_BUTTON = By.xpath("//input[@class='a-button-input']");
    private static final By PASSWORD_INPUT = By.id("ap_password");
    private static final By SIGN_IN_BUTTON = By.id("signInSubmit");
    private static final By EMAIL_MISSING_ALERT = By.cssSelector("#auth-email-missing-alert .a-alert-content");
    private static final By PASSWORD_MISSING_ALERT = By.cssSelector("#auth-password-missing-alert .a-alert-content");
    private static final By ERROR_BOX = By.cssSelector("#auth-error-message-box .a-alert-content, .a-alert-error .a-alert-content");
    private static final By PAGE_HEADING = By.cssSelector("h1.a-spacing-small");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        new AmazonHomePage(driver).open(baseUrl).openSignIn();
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(EMAIL_INPUT) || isDisplayed(PASSWORD_INPUT);
    }

    public LoginPage enterEmailAndContinue(String emailOrMobile) {
        type(EMAIL_INPUT, emailOrMobile);
        click(CONTINUE_BUTTON);
        return this;
    }

    public LoginPage submitBlankEmail() {
        click(CONTINUE_BUTTON);
        return this;
    }

    public LoginPage submitBlankPassword() {
        click(SIGN_IN_BUTTON);
        return this;
    }

    public boolean isPasswordStepDisplayed() {
        return isDisplayed(PASSWORD_INPUT);
    }

    public String getHeading() {
        return isDisplayed(PAGE_HEADING) ? textOf(PAGE_HEADING) : "";
    }

    public String getValidationMessage() {
        if (isDisplayed(EMAIL_MISSING_ALERT)) {
            return textOf(EMAIL_MISSING_ALERT);
        }
        if (isDisplayed(PASSWORD_MISSING_ALERT)) {
            return textOf(PASSWORD_MISSING_ALERT);
        }
        if (isDisplayed(ERROR_BOX)) {
            return textOf(ERROR_BOX).replaceAll("\\s+", " ");
        }
        return "";
    }
}