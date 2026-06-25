package com.resumeqa.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public final class ExtentManager {
    private static ExtentReports extentReports;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extentReports == null) {
            try {
                Path reportDir = Path.of("test-output", "ExtentReports");
                Files.createDirectories(reportDir);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                ExtentSparkReporter reporter = new ExtentSparkReporter(reportDir.resolve("AutomationReport_" + timestamp + ".html").toString());
                reporter.config().setDocumentTitle("Resume Automation Report");
                reporter.config().setReportName("Login, Add to Cart, Checkout Regression Suite");
                reporter.config().setTheme(Theme.STANDARD);

                extentReports = new ExtentReports();
                extentReports.attachReporter(reporter);
                extentReports.setSystemInfo("Framework", "Selenium + TestNG + Apache POI + POM");
                extentReports.setSystemInfo("Application", ConfigReader.get("baseUrl"));
            } catch (Exception e) {
                throw new IllegalStateException("Unable to initialize Extent Reports", e);
            }
        }
        return extentReports;
    }
}
