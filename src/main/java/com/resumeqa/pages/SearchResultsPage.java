package com.resumeqa.pages;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsPage extends BasePage {
    private static final By RESULTS = By.cssSelector("div[data-component-type='s-search-result']");
    private static final By RESULT_TITLE_LINK = By.cssSelector("h2 a, a.a-link-normal.s-line-clamp-2, a.a-link-normal.s-line-clamp-4");
    private static final By RESULT_PRICE = By.cssSelector(".a-price .a-offscreen");
    private static final By SEARCH_RESULT_TEXT = By.cssSelector("span.a-color-state, .a-section.a-spacing-small.a-spacing-top-small span");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(RESULTS) || isRobotCheckDisplayed();
    }

    public int getResultCount() {
        if (count(RESULTS) == 0) {
            isLoaded();
        }
        return count(RESULTS);
    }

    public String getResultSummaryText() {
        return isDisplayed(SEARCH_RESULT_TEXT) ? textOf(SEARCH_RESULT_TEXT) : driver.getTitle();
    }

    public boolean hasPricedResult() {
        return count(RESULT_PRICE) > 0;
    }

    public ProductDetailsPage openResult(int oneBasedIndex) {
        List<WebElement> results = driver.findElements(RESULTS);
        if (results.isEmpty()) {
            throw new IllegalStateException("No Amazon search results found. Page may be blocked by CAPTCHA or layout changed.");
        }
        int index = Math.max(0, Math.min(oneBasedIndex - 1, results.size() - 1));
        WebElement title = results.get(index).findElement(RESULT_TITLE_LINK);
        scrollIntoView(title);
        Set<String> handles = driver.getWindowHandles();
        title.click();
        if (driver.getWindowHandles().size() > handles.size()) {
            switchToNewestWindow(handles);
        }
        return new ProductDetailsPage(driver);
    }
}