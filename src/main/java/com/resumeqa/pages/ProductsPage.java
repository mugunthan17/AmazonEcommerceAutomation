package com.resumeqa.pages;

import org.openqa.selenium.WebDriver;

/**
 * Backward-compatible alias retained so older imports still compile.
 * New Amazon tests use SearchResultsPage directly.
 */
public class ProductsPage extends SearchResultsPage {
    public ProductsPage(WebDriver driver) {
        super(driver);
    }
}