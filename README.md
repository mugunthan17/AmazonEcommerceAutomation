# Amazon.in Selenium TestNG DDT Framework

This project automates Amazon India ecommerce workflows with Selenium, TestNG, Apache POI, Page Object Model, and Extent Reports.

## Resume Coverage

- Designed and automated end-to-end test scenarios for Login/Sign-in entry, Add to Cart, and Checkout entry using Selenium and TestNG.
- Covers 35 functional and regression test cases:
  - `login` sheet: 10 sign-in/validation cases
  - `addToCart` sheet: 15 search, product details, add-to-cart, cart, and remove cases
  - `checkout` sheet: 10 cart validation and checkout sign-in gate cases
- Built a Data-Driven Testing framework using Apache POI and `testData.xlsx`.
- Implemented Page Object Model pages for Amazon Home, Login, Search Results, Product Details, Add to Cart, Cart, and Checkout.
- Added Extent Reports with screenshot capture for structured defect analysis.

## Important Amazon Checkout Boundary

The framework validates cart-to-checkout entry and the Amazon sign-in gate. It intentionally does not place real orders or automate payment submission.

Amazon may show CAPTCHA, OTP, or robot verification. If that appears, the affected test fails with a clear message because bypassing those controls is not part of legitimate test automation.

## Project Structure

```text
src/main/java/com/resumeqa/pages
src/main/java/com/resumeqa/utils
src/main/resources/config.properties
src/main/resources/drivers/chromedriver.exe
src/test/java/com/resumeqa/tests
src/test/resources/testData.xlsx
```

## Run

Place ChromeDriver here if you do not want Selenium Manager to resolve it:

```text
src/main/resources/drivers/chromedriver.exe
```

Then run:

```bash
mvn clean test
```

Optional:

```bash
mvn clean test -DbaseUrl=https://www.amazon.in/ -Dbrowser=chrome -Dheadless=false
```

Reports are generated in:

```text
test-output/ExtentReports/
```