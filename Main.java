import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Cookie;


import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Set path to chromedriver
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            driver.get("https://syn-int-suite-ent-qgjsk2hi.integrationsuite.cfapps.eu10-002.hana.ondemand.com");

            // Click "Default Identity Provider"
            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Default Identity Provider"))).click();

            // Enter username
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_username"))).sendKeys("S0023825691");

            // Click continue after username
            wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();

            // Enter password
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("j_password"))).sendKeys("Syngenta@21");

            // Click continue after password
            wait.until(ExpectedConditions.elementToBeClickable(By.id("logOnFormSubmit"))).click();

            // Optional: wait or verify login success here
            Thread.sleep(5000);


                        // Wait 7 seconds like in your Python code
            Thread.sleep(7000);

            // Get cookies
            Set<Cookie> cookies = driver.manage().getCookies();

            // Convert cookies to a single string "name=value; name2=value2; ..."
            String cookieHeader = cookies.stream()
                    .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                    .collect(Collectors.joining("; "));

            System.out.println("Cookies: " + cookieHeader);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
