package com.resumeqa.tests;

import java.util.Map;

import com.resumeqa.pages.AddToCartPage;
import com.resumeqa.pages.AmazonHomePage;
import com.resumeqa.pages.CartPage;
import com.resumeqa.pages.CheckoutPage;
import com.resumeqa.pages.ProductDetailsPage;
import com.resumeqa.pages.SearchResultsPage;
import com.resumeqa.utils.ExcelUtils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {
    @DataProvider(name = "checkoutData")
    public Object[][] checkoutData() {
        return ExcelUtils.getSheetData("checkout");
    }

    @Test(dataProvider = "checkoutData", description = "Validates Amazon cart-to-checkout entry scenarios from Excel")
    public void verifyCheckoutScenarios(Map<String, String> data) {
        Map<String, String> cartData = ExcelUtils.findById("addToCart", "caseId", data.get("addToCartCaseId"));

        AmazonHomePage homePage = new AmazonHomePage(driver).open(baseUrl);
        Assert.assertFalse(homePage.isRobotCheckDisplayed(), "Amazon displayed robot verification. Manual verification is required.");

        SearchResultsPage resultsPage = homePage.searchFor(cartData.get("searchTerm"));
        Assert.assertTrue(resultsPage.isLoaded(), "Search results should load");

        ProductDetailsPage productPage = resultsPage.openResult(Integer.parseInt(cartData.get("productIndex")));
        Assert.assertTrue(productPage.isLoaded(), "Product details should load");
        Assert.assertTrue(productPage.canAddToCart(), "Product should support Add to Cart before checkout validation");

        AddToCartPage addToCartPage = productPage.addToCart();
        CartPage cartPage = addToCartPage.openCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart should load before checkout");
        Assert.assertTrue(cartPage.hasCheckoutButton(), "Proceed to checkout button should be available");

        if ("CART_VALIDATION".equalsIgnoreCase(data.get("action"))) {
            Assert.assertTrue(cartPage.getCartItemCount() >= 1 || !cartPage.getSubtotalText().isBlank(), data.get("description"));
            return;
        }

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        Assert.assertTrue(checkoutPage.isLoaded(), "Checkout or sign-in gate should load");
        Assert.assertFalse(checkoutPage.isPlaceOrderButtonDisplayed(), "Automation must not place a real Amazon order");

        if ("EXPECT_SIGN_IN".equalsIgnoreCase(data.get("expectedResult"))) {
            Assert.assertTrue(checkoutPage.isSignInGateDisplayed(), data.get("description"));
            Assert.assertTrue(checkoutPage.getPageMessage().toLowerCase().contains(data.get("expectedText").toLowerCase())
                    || checkoutPage.isSignInGateDisplayed(), "Sign-in gate should be visible before checkout");
        } else {
            Assert.assertTrue(checkoutPage.isCheckoutPageDisplayed() || checkoutPage.isSignInGateDisplayed(), data.get("description"));
        }
    }
}