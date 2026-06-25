package com.resumeqa.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final ExtentReports EXTENT = ExtentManager.getExtentReports();
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        TEST.set(EXTENT.createTest(result.getMethod().getMethodName())
                .assignCategory(result.getTestClass().getRealClass().getSimpleName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TEST.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        TEST.get().log(Status.FAIL, result.getThrowable());
        try {
            WebDriver driver = DriverFactory.getDriver();
            String screenshot = ScreenshotUtils.capture(driver, result.getMethod().getMethodName());
            if (!screenshot.isBlank()) {
                TEST.get().addScreenCaptureFromPath(screenshot);
            }
        } catch (RuntimeException ignored) {
            TEST.get().info("Screenshot unavailable because WebDriver was not active");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TEST.get().log(Status.SKIP, result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        EXTENT.flush();
        TEST.remove();
    }
}
