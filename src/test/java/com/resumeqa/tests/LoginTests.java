package com.resumeqa.tests;

import java.util.Map;

import com.resumeqa.pages.AmazonHomePage;
import com.resumeqa.pages.LoginPage;
import com.resumeqa.utils.ExcelUtils;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return ExcelUtils.getSheetData("login");
    }

    @Test(dataProvider = "loginData", description = "Validates Amazon sign-in entry scenarios from Excel")
    public void verifyLoginScenarios(Map<String, String> data) {
        AmazonHomePage homePage = new AmazonHomePage(driver).open(baseUrl);
        Assert.assertTrue(homePage.isLoaded() || homePage.isRobotCheckDisplayed(), "Amazon home page should load or show verification gate");
        if (homePage.isRobotCheckDisplayed()) {
            Assert.fail("Amazon displayed robot verification. Manual verification is required before automation can continue.");
        }

        LoginPage loginPage = homePage.openSignIn();
        long start = System.currentTimeMillis();
        boolean found = false;
        for (int i = 0; i < 40; i++) {
            int count = driver.findElements(org.openqa.selenium.By.id("ap_email")).size();
            if (count > 0) {
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("DEBUG FOUND ap_email after " + elapsed + " ms (" + i + " polls)");
                found = true;
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
        if (!found) {
            System.out.println("DEBUG: ap_email never appeared within 40 seconds");
        }
        java.util.List<org.openqa.selenium.WebElement> emailEls = driver.findElements(org.openqa.selenium.By.id("ap_email"));
        System.out.println("DEBUG EMAIL ELEMENT COUNT: " + emailEls.size());
        if (!emailEls.isEmpty()) {
            System.out.println("DEBUG EMAIL DISPLAYED: " + emailEls.get(0).isDisplayed());
            System.out.println("DEBUG EMAIL SIZE: " + emailEls.get(0).getSize());
            System.out.println("DEBUG EMAIL LOCATION: " + emailEls.get(0).getLocation());
        }
        if (loginPage.isRobotCheckDisplayed()) {
            Assert.fail("Amazon displayed robot verification on the sign-in page. Headless automation is being challenged here.");
        }
        Assert.assertTrue(loginPage.isLoaded(), "Amazon sign-in page should be displayed");

        String action = data.get("action");
        if ("OPEN_SIGN_IN".equalsIgnoreCase(action)) {
            Assert.assertTrue(loginPage.getHeading().toLowerCase().contains(data.get("expectedText").toLowerCase()), data.get("description"));
            return;
        }

        if ("BLANK_EMAIL".equalsIgnoreCase(action)) {
            loginPage.submitBlankEmail();
        } else {
            loginPage.enterEmailAndContinue(data.get("emailOrMobile"));
        }

        if ("PASSWORD_STEP".equalsIgnoreCase(data.get("expectedResult"))) {
            Assert.assertTrue(loginPage.isPasswordStepDisplayed() || !loginPage.getValidationMessage().isBlank(), data.get("description"));
        } else {
            Assert.assertTrue(loginPage.getValidationMessage().toLowerCase().contains(data.get("expectedText").toLowerCase()), data.get("description"));
        }
    }
}
