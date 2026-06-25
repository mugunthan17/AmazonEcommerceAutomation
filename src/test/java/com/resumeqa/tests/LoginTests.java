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