import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Cookie;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashMap;
import com.google.gson.Gson; // This import is now resolved via pom.xml

public class Main {
    public static void main(String[] args) {
        // Retrieve sensitive data from environment variables
        String url = System.getenv("TARGET_URL");
        String username = System.getenv("LOGIN_USERNAME");
        String password = System.getenv("LOGIN_PASSWORD");

        // Basic validation
        if (url == null || username == null || password == null) {
            System.err.println("ERROR: Missing environment variables (TARGET_URL, LOGIN_USERNAME, LOGIN_PASSWORD). Ensure GitHub Secrets are configured.");
            System.exit(1); // Exit with a non-zero code to indicate failure
        }

        // The chromedriver path is set by the browser-actions/setup-chrome action
        // so you DO NOT need System.setProperty("webdriver.chrome.driver", "...") here.

        // Enable headless mode and add necessary arguments for Linux/Docker environments
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox"); // Essential for Linux environments like GitHub Actions
        options.addArguments("--disable-dev-shm-usage"); // Recommended for Docker/Linux headless

        WebDriver driver = null; // Declare driver outside try-catch to ensure it's in scope for finally
        try {
            System.err.println("Attempting to initialize ChromeDriver..."); // Log to stderr for GitHub Actions log
            driver = new ChromeDriver(options);
            System.err.println("ChromeDriver initialized.");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased wait time for robustness

            driver.get(url);
            System.err.println("Navigated to URL: " + url);

            // Click "Default Identity Provider" - Adjust selectors as needed for your specific login page
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Default Identity Provider"))).click();
            System.err.println("Clicked 'Default Identity Provider'.");

            // Enter username
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username"))).sendKeys(username);
            System.err.println("Entered username.");

            // Click continue after username
            wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
            System.err.println("Clicked continue after username.");

            // Enter password
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_password"))).sendKeys(password);
            System.err.println("Entered password.");

            // Click continue after password
            wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();
            System.err.println("Clicked continue after password.");

            // Wait for the page to load completely after login. Adjust this duration based on observed behavior.
            Thread.sleep(10000); // 10 seconds. Consider using explicit waits if possible for specific elements.
            System.err.println("Waited for post-login page load.");

            // Get cookies
            Set<Cookie> cookies = driver.manage().getCookies();

            // Convert cookies to a Map for JSON serialization (key-value pairs)
            HashMap<String, String> cookieMap = new HashMap<>();
            cookies.forEach(cookie -> cookieMap.put(cookie.getName(), cookie.getValue()));

            System.err.println("Captured " + cookies.size() + " cookies.");

            // Use Gson to convert the Map to a JSON string
            Gson gson = new Gson();
            String jsonCookies = gson.toJson(cookieMap);

            // Output the JSON string to standard output (stdout).
            // GitHub Actions will capture this exact line for the 'cookies' output.
            System.out.println(jsonCookies); 

        } catch (Exception e) {
            System.err.println("Selenium operation failed: " + e.getMessage());
            e.printStackTrace(System.err); // Print full stack trace to stderr for debugging
            System.exit(1); // Exit with a non-zero code to indicate failure to GitHub Actions
        } finally {
            if (driver != null) {
                System.err.println("Quitting WebDriver.");
                driver.quit();
            }
        }
    }
}