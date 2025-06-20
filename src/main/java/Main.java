
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static void takeScreenshot(WebDriver driver, String filename) {
        if (driver instanceof TakesScreenshot) {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                Files.createDirectories(Paths.get("screenshots"));
                File destFile = new File("screenshots/" + filename);
                Files.copy(srcFile.toPath(), destFile.toPath());
                System.err.println("Screenshot saved: " + destFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to save screenshot: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    private static void logPageSource(WebDriver driver, String stepName) {
        System.err.println("--- Page Source at " + stepName + " ---");
        System.err.println(driver.getPageSource().substring(0, Math.min(driver.getPageSource().length(), 2000)));
        System.err.println("--- End Page Source ---");
    }

    public static void main(String[] args) {
        String url = System.getenv("TARGET_URL");
        String username = System.getenv("LOGIN_USERNAME");
        String password = System.getenv("LOGIN_PASSWORD");

        if (url == null || username == null || password == null) {
            System.err.println("ERROR: Missing environment variables (TARGET_URL, LOGIN_USERNAME, LOGIN_PASSWORD). " +
                               "Ensure GitHub Secrets are configured and passed correctly in the workflow.");
            System.exit(1);
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = null;
        try {
            System.err.println("Attempting to initialize ChromeDriver with headless options...");
            driver = new ChromeDriver(options);
            System.err.println("ChromeDriver initialized successfully.");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            driver.get(url);
            System.err.println("Navigated to URL: " + url);
            takeScreenshot(driver, "1_initial_page.png");
            logPageSource(driver, "Initial Page Load");

            System.err.println("Attempting to click 'Default Identity Provider'...");
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Default Identity Provider"))).click();
            System.err.println("Clicked 'Default Identity Provider'.");
            takeScreenshot(driver, "2_after_default_idp_click.png");
            logPageSource(driver, "After Default IDP Click");

            System.err.println("Attempting to enter username into field with ID 'j_username'...");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username"))).sendKeys(username);
            System.err.println("Entered username.");
            takeScreenshot(driver, "3_username_entered.png");
            logPageSource(driver, "After Username Entry");

            System.err.println("Attempting to click continue button with ID 'logOnFormSubmit' after username...");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
            System.err.println("Clicked continue after username.");
            takeScreenshot(driver, "4_after_username_continue.png");
            logPageSource(driver, "After Username Continue Click");

            System.err.println("Attempting to enter password into field with ID 'j_password'...");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_password"))).sendKeys(password);
            System.err.println("Entered password.");
            takeScreenshot(driver, "5_password_entered.png");
            logPageSource(driver, "After Password Entry");

            System.err.println("Attempting to click continue button with ID 'logOnFormSubmit' after password...");
            wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
            System.err.println("Clicked continue after password.");
            takeScreenshot(driver, "6_after_password_continue.png");
            logPageSource(driver, "After Password Continue Click");

            System.err.println("Waiting for post-login page load (7 seconds)...");
            Thread.sleep(7000);
            System.err.println("Waited for post-login page load.");
            takeScreenshot(driver, "7_post_login_page.png");
            logPageSource(driver, "Post Login Page");

            Set<Cookie> cookies = driver.manage().getCookies();
            System.err.println("Captured " + cookies.size() + " cookies.");

            String cookieHeader = cookies.stream()
                    .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                    .collect(Collectors.joining("; "));

            System.out.println(cookieHeader);

        } catch (Exception e) {
            System.err.println("Selenium operation failed: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } finally {
            if (driver != null) {
                System.err.println("Quitting WebDriver.");
                driver.quit();
            }
        }
    }
}
