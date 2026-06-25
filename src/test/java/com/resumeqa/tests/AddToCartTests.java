package com.resumeqa.tests;

import java.util.Map;

import com.resumeqa.pages.AddToCartPage;
import com.resumeqa.pages.AmazonHomePage;
import com.resumeqa.pages.CartPage;
import com.resumeqa.pages.ProductDetailsPage;
import com.resumeqa.pages.SearchResultsPage;
import com.resumeqa.utils.ExcelUtils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddToCartTests extends BaseTest {
    @DataProvider(name = "addToCartData")
    public Object[][] addToCartData() {
        return ExcelUtils.getSheetData("addToCart");
    }

    @Test(dataProvider = "addToCartData", description = "Validates Amazon search and add-to-cart scenarios from Excel")
    public void verifyAddToCartScenarios(Map<String, String> data) {
        AmazonHomePage homePage = new AmazonHomePage(driver).open(baseUrl);
        Assert.assertFalse(homePage.isRobotCheckDisplayed(), "Amazon displayed robot verification. Manual verification is required.");

        SearchResultsPage resultsPage = homePage.searchFor(data.get("searchTerm"));
        Assert.assertTrue(resultsPage.isLoaded(), "Search results should load for: " + data.get("searchTerm"));
        Assert.assertTrue(resultsPage.getResultCount() >= Integer.parseInt(data.get("expectedMinResults")), data.get("description"));

        if ("SEARCH_ONLY".equalsIgnoreCase(data.get("action"))) {
            Assert.assertTrue(resultsPage.getResultSummaryText().toLowerCase().contains(data.get("expectedKeyword").toLowerCase())
                    || driver.getTitle().toLowerCase().contains(data.get("expectedKeyword").toLowerCase()), data.get("description"));
            return;
        }

        ProductDetailsPage productPage = resultsPage.openResult(Integer.parseInt(data.get("productIndex")));
        Assert.assertTrue(productPage.isLoaded(), "Product details should load");
        Assert.assertTrue(productPage.getProductTitle().toLowerCase().contains(data.get("expectedKeyword").toLowerCase())
                || !productPage.getProductTitle().isBlank(), "Product title should be visible");

        if ("PRODUCT_DETAILS".equalsIgnoreCase(data.get("action"))) {
            Assert.assertTrue(productPage.canAddToCart() || productPage.canBuyNow(), "Product page should expose purchase controls");
            return;
        }

        Assert.assertTrue(productPage.canAddToCart(), "Product must have an Add to Cart button for this scenario");
        AddToCartPage addToCartPage = productPage.addToCart();
        Assert.assertTrue(addToCartPage.isLoaded(), "Add to cart confirmation should load");
        Assert.assertTrue(addToCartPage.isSuccessMessageDisplayed() || addToCartPage.getHeaderCartCount() >= Integer.parseInt(data.get("expectedCartCount")), data.get("description"));

        if ("OPEN_CART".equalsIgnoreCase(data.get("action")) || "REMOVE_FROM_CART".equalsIgnoreCase(data.get("action"))) {
            CartPage cartPage = addToCartPage.openCart();
            Assert.assertTrue(cartPage.isLoaded(), "Cart page should load after adding product");
            Assert.assertTrue(cartPage.getCartItemCount() >= 1 || !cartPage.getSubtotalText().isBlank(), "Cart should contain at least one item or subtotal");
            if ("REMOVE_FROM_CART".equalsIgnoreCase(data.get("action"))) {
                cartPage.removeFirstItemIfPresent();
                Assert.assertTrue(cartPage.isLoaded(), "Cart should remain accessible after remove action");
            }
        }
    }
}