package com.resumeqa.tests;

import com.resumeqa.utils.ConfigReader;
import com.resumeqa.utils.DriverFactory;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    protected WebDriver driver;
    protected String baseUrl;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver();
        baseUrl = ConfigReader.get("baseUrl");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
