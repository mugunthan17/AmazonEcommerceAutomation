package com.resumeqa.pages;

import java.time.Duration;
import java.util.Set;

import com.resumeqa.utils.ConfigReader;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicitWaitSeconds")));
        PageFactory.initElements(driver, this);
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value == null ? "" : value);
    }

    protected void click(By locator) {
        clickable(locator).click();
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected String textOf(By locator) {
        return visible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected int count(By locator) {
        return driver.findElements(locator).size();
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void switchToNewestWindow(Set<String> oldHandles) {
        wait.until(webDriver -> webDriver.getWindowHandles().size() > oldHandles.size());
        for (String handle : driver.getWindowHandles()) {
            if (!oldHandles.contains(handle)) {
                driver.switchTo().window(handle);
                return;
            }
        }
    }

    public boolean isRobotCheckDisplayed() {
        String page = driver.getPageSource().toLowerCase();
        return page.contains("not a robot") || page.contains("captcha") || page.contains("enter the characters you see below");
    }
}