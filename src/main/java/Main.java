// // import org.openqa.selenium.By;
// // import org.openqa.selenium.WebDriver;
// // import org.openqa.selenium.chrome.ChromeDriver;
// // import org.openqa.selenium.chrome.ChromeOptions;
// // import org.openqa.selenium.support.ui.ExpectedConditions;
// // import org.openqa.selenium.support.ui.WebDriverWait;
// // import org.openqa.selenium.Cookie;

// // import java.time.Duration;
// // import java.util.Set;
// // import java.util.stream.Collectors;
// // import java.util.HashMap;
// // import com.google.gson.Gson; // This import is now resolved via pom.xml

// // public class Main {
// //     public static void main(String[] args) {
// //         // Retrieve sensitive data from environment variables
// //         String url = System.getenv("TARGET_URL");
// //         String username = System.getenv("LOGIN_USERNAME");
// //         String password = System.getenv("LOGIN_PASSWORD");

// //         // Basic validation
// //         if (url == null || username == null || password == null) {
// //             System.err.println("ERROR: Missing environment variables (TARGET_URL, LOGIN_USERNAME, LOGIN_PASSWORD). Ensure GitHub Secrets are configured.");
// //             System.exit(1); // Exit with a non-zero code to indicate failure
// //         }

// //         // The chromedriver path is set by the browser-actions/setup-chrome action
// //         // so you DO NOT need System.setProperty("webdriver.chrome.driver", "...") here.

// //         // Enable headless mode and add necessary arguments for Linux/Docker environments
// //         ChromeOptions options = new ChromeOptions();
// //         options.addArguments("--headless");
// //         options.addArguments("--disable-gpu");
// //         options.addArguments("--window-size=1920,1080");
// //         options.addArguments("--no-sandbox"); // Essential for Linux environments like GitHub Actions
// //         options.addArguments("--disable-dev-shm-usage"); // Recommended for Docker/Linux headless

// //         WebDriver driver = null; // Declare driver outside try-catch to ensure it's in scope for finally
// //         try {
// //             System.err.println("Attempting to initialize ChromeDriver..."); // Log to stderr for GitHub Actions log
// //             driver = new ChromeDriver(options);
// //             System.err.println("ChromeDriver initialized.");

// //             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased wait time for robustness

// //             driver.get(url);
// //             System.err.println("Navigated to URL: " + url);

// //             // Click "Default Identity Provider" - Adjust selectors as needed for your specific login page
// //             wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Default Identity Provider"))).click();
// //             System.err.println("Clicked 'Default Identity Provider'.");

// //             // Enter username
// //             wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username"))).sendKeys(username);
// //             System.err.println("Entered username.");

// //             // Click continue after username
// //             wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
// //             System.err.println("Clicked continue after username.");

// //             // Enter password
// //             wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_password"))).sendKeys(password);
// //             System.err.println("Entered password.");

// //             // Click continue after password
// //             wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
// //             System.err.println("Clicked continue after password.");

// //             // Wait for the page to load completely after login. Adjust this duration based on observed behavior.
// //             Thread.sleep(10000); // 10 seconds. Consider using explicit waits if possible for specific elements.
// //             System.err.println("Waited for post-login page load.");

// //             // Get cookies
// //             Set<Cookie> cookies = driver.manage().getCookies();

// //             // Convert cookies to a Map for JSON serialization (key-value pairs)
// //             HashMap<String, String> cookieMap = new HashMap<>();
// //             cookies.forEach(cookie -> cookieMap.put(cookie.getName(), cookie.getValue()));

// //             System.err.println("Captured " + cookies.size() + " cookies.");

// //             // Use Gson to convert the Map to a JSON string
// //             Gson gson = new Gson();
// //             String jsonCookies = gson.toJson(cookieMap);

// //             // Output the JSON string to standard output (stdout).
// //             // GitHub Actions will capture this exact line for the 'cookies' output.
// //             System.out.println(jsonCookies); 

// //         } catch (Exception e) {
// //             System.err.println("Selenium operation failed: " + e.getMessage());
// //             e.printStackTrace(System.err); // Print full stack trace to stderr for debugging
// //             System.exit(1); // Exit with a non-zero code to indicate failure to GitHub Actions
// //         } finally {
// //             if (driver != null) {
// //                 System.err.println("Quitting WebDriver.");
// //                 driver.quit();
// //             }
// //         }
// //     }
// // }

// // ********************************************************************************


// import org.openqa.selenium.By;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;
// import org.openqa.selenium.support.ui.ExpectedConditions;
// import org.openqa.selenium.support.ui.WebDriverWait;
// import org.openqa.selenium.Cookie;
// import org.openqa.selenium.OutputType;
// import org.openqa.selenium.TakesScreenshot;

// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.time.Duration;
// import java.util.Set;
// import java.util.stream.Collectors;

// public class Main {

//     /**
//      * Takes a screenshot of the current browser view and saves it to the 'screenshots' directory.
//      *
//      * @param driver   The WebDriver instance.
//      * @param filename The name of the file to save the screenshot as (e.g., "login_page.png").
//      */
//     private static void takeScreenshot(WebDriver driver, String filename) {
//         if (driver instanceof TakesScreenshot) {
//             File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//             try {
//                 // Ensure the 'screenshots' directory exists in the current working directory
//                 Files.createDirectories(Paths.get("screenshots"));
//                 File destFile = new File("screenshots/" + filename);
//                 Files.copy(srcFile.toPath(), destFile.toPath());
//                 System.err.println("Screenshot saved: " + destFile.getAbsolutePath());
//             } catch (IOException e) {
//                 System.err.println("Failed to save screenshot: " + e.getMessage());
//                 e.printStackTrace(System.err);
//             }
//         }
//     }

//     /**
//      * Logs a truncated version of the current page's HTML source to standard error.
//      * This helps in debugging element presence.
//      *
//      * @param driver   The WebDriver instance.
//      * @param stepName A descriptive name for the current step.
//      */
//     private static void logPageSource(WebDriver driver, String stepName) {
//         System.err.println("--- Page Source at " + stepName + " ---");
//         // Log the first 2000 characters of the page source for brevity in logs
//         System.err.println(driver.getPageSource().substring(0, Math.min(driver.getPageSource().length(), 2000)));
//         System.err.println("--- End Page Source ---");
//     }

//     public static void main(String[] args) {
//         // Retrieve sensitive data from environment variables for security
//         // These will be set in the GitHub Actions workflow
//         String url = System.getenv("TARGET_URL");
//         String username = System.getenv("LOGIN_USERNAME");
//         String password = System.getenv("LOGIN_PASSWORD");

//         // Basic validation for environment variables
//         if (url == null || username == null || password == null) {
//             System.err.println("ERROR: Missing environment variables (TARGET_URL, LOGIN_USERNAME, LOGIN_PASSWORD). " +
//                                "Ensure GitHub Secrets are configured and passed correctly in the workflow.");
//             System.exit(1); // Exit with a non-zero code to indicate failure
//         }

//         // The chromedriver path is automatically handled by browser-actions/setup-chrome
//         // so System.setProperty("webdriver.chrome.driver", "...") is NOT needed here.

//         // Configure ChromeOptions for headless execution and Linux environment compatibility
//         ChromeOptions options = new ChromeOptions();
//         options.addArguments("--headless"); // Run Chrome in headless mode (no UI)
//         options.addArguments("--disable-gpu"); // Recommended for headless on Linux
//         options.addArguments("--window-size=1920,1080"); // Set a consistent window size
//         options.addArguments("--no-sandbox"); // Essential for Linux environments like GitHub Actions
//         options.addArguments("--disable-dev-shm-usage"); // Recommended for Docker/Linux headless environments

//         WebDriver driver = null; // Declare driver outside try-catch for finally block access
//         try {
//             System.err.println("Attempting to initialize ChromeDriver with headless options...");
//             driver = new ChromeDriver(options); // Initialize ChromeDriver with configured options
//             System.err.println("ChromeDriver initialized successfully.");

//             // Initialize WebDriverWait for explicit waits (up to 30 seconds)
//             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

//             // Navigate to the target URL
//             driver.get(url);
//             System.err.println("Navigated to URL: " + url);
//             takeScreenshot(driver, "1_initial_page.png"); // Capture initial page state
//             logPageSource(driver, "Initial Page Load"); // Log initial page source

//             // Step 1: Click "Default Identity Provider" link
//             System.err.println("Attempting to click 'Default Identity Provider'...");
//             wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Default Identity Provider"))).click();
//             System.err.println("Clicked 'Default Identity Provider'.");
//             takeScreenshot(driver, "2_after_default_idp_click.png"); // Capture state after click
//             logPageSource(driver, "After Default IDP Click"); // Log page source after click

//             // Step 2: Enter username
//             System.err.println("Attempting to enter username into field with ID 'j_username'...");
//             wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username"))).sendKeys(username);
//             System.err.println("Entered username.");
//             takeScreenshot(driver, "3_username_entered.png"); // Capture state after username entry
//             logPageSource(driver, "After Username Entry"); // Log page source after entry

//             // Step 3: Click continue after username
//             System.err.println("Attempting to click continue button with ID 'logOnFormSubmit' after username...");
//             wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
//             System.err.println("Clicked continue after username.");
//             takeScreenshot(driver, "4_after_username_continue.png"); // Capture state after continue click
//             logPageSource(driver, "After Username Continue Click"); // Log page source after click

//             // Step 4: Enter password
//             System.err.println("Attempting to enter password into field with ID 'j_password'...");
//             wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_password"))).sendKeys(password);
//             System.err.println("Entered password.");
//             takeScreenshot(driver, "5_password_entered.png"); // Capture state after password entry
//             logPageSource(driver, "After Password Entry"); // Log page source after entry

//             // Step 5: Click continue after password
//             System.err.println("Attempting to click continue button with ID 'logOnFormSubmit' after password...");
//             wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
//             System.err.println("Clicked continue after password.");
//             takeScreenshot(driver, "6_after_password_continue.png"); // Capture state after continue click
//             logPageSource(driver, "After Password Continue Click"); // Log page source after click

//             // Optional: Wait for the page to load completely after login.
//             // Adjust this duration based on observed behavior of your application.
//             System.err.println("Waiting for post-login page load (7 seconds)...");
//             Thread.sleep(7000); // Wait for 7 seconds as in your working code
//             System.err.println("Waited for post-login page load.");
//             takeScreenshot(driver, "7_post_login_page.png"); // Capture final page state
//             logPageSource(driver, "Post Login Page"); // Log final page source

//             // Get all cookies from the current session
//             Set<Cookie> cookies = driver.manage().getCookies();
//             System.err.println("Captured " + cookies.size() + " cookies.");

//             // Convert cookies to a single string in the format "name=value; name2=value2; ..."
//             // This matches the format you used in your working code.
//             String cookieHeader = cookies.stream()
//                     .map(cookie -> cookie.getName() + "=" + cookie.getValue())
//                     .collect(Collectors.joining("; "));

//             // Output the cookie string to standard output (stdout).
//             // GitHub Actions will capture this line for the 'cookies' output.
//             System.out.println(cookieHeader);

//         } catch (Exception e) {
//             // Log any exceptions that occur during Selenium operations
//             System.err.println("Selenium operation failed: " + e.getMessage());
//             e.printStackTrace(System.err); // Print full stack trace to stderr for detailed debugging
//             System.exit(1); // Exit with a non-zero code to indicate failure to GitHub Actions
//         } finally {
//             // Ensure the WebDriver is quit to release resources, even if an error occurs
//             if (driver != null) {
//                 System.err.println("Quitting WebDriver.");
//                 driver.quit();
//             }
//         }
//     }
// }






// Your existing Main.java code - NO CHANGES REQUIRED
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