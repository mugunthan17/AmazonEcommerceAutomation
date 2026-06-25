package com.resumeqa.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtils {
    private ScreenshotUtils() {
    }

    public static String capture(WebDriver driver, String testName) {
        try {
            Path screenshotDir = Path.of("test-output", "screenshots");
            Files.createDirectories(screenshotDir);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            Path destination = screenshotDir.resolve(testName.replaceAll("[^a-zA-Z0-9._-]", "_") + "_" + timestamp + ".png");
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination);
            return destination.toAbsolutePath().toString();
        } catch (IOException | RuntimeException e) {
            return "";
        }
    }
}
