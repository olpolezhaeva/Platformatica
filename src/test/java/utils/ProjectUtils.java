package utils;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class ProjectUtils {

    public static final String LOGIN_PROP = "default.username";
    public static final String PAS_PROP = "default.password";

    private static Properties getCredentials(WebDriver driver) {

        Properties properties = new Properties();
        if (BaseTest.isRemoteWebDriver()) {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL("https://ref2.eteam.work/next_tester.php").openConnection();
                try {
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String response = in.readLine();
                        String[] responseArray = response.split(";");

                        properties.setProperty(LOGIN_PROP, responseArray[0]);
                        properties.setProperty(PAS_PROP, responseArray[1]);
                    }
                } finally {
                    con.disconnect();
                }
            } catch (IOException ignore) {
            }
        } else {
            try {
                InputStream inputStream = ProjectUtils.class.getClassLoader().getResourceAsStream("local.properties");
                if (inputStream == null) {
                    System.out.println("ERROR: The local.properties file not found in src/test/resources/ directory.");
                    System.out.println("You need to create it from local.properties.TEMPLATE file.");
                    System.out.println("Please see https://youtu.be/gsicxtw-x34?t=1866 for instructions.");
                    driver.quit();
                    System.exit(1);
                }
                properties.load(inputStream);
            } catch (IOException ignore) {
            }
        }
        return properties;
    }

    public static void login(WebDriver driver) {
        Properties properties = getCredentials(driver);
        login(driver, properties.getProperty(LOGIN_PROP), properties.getProperty(PAS_PROP));
    }

    public static void login(WebDriver driver, String login, String pas) {
        driver.findElement(By.name("login_name")).sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(pas);
        driver.findElement(By.cssSelector("button[type=submit]")).click();
    }

    public static void reset(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver.findElement(By.id("navbarDropdownProfile")).click();
        TestUtils.jsClick(driver,
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(),'!!! Reset all for my user !!!')]"))));
    }

    public static void get(WebDriver driver) {
        driver.get("https://ref2.eteam.work/");
    }

    public static void start(WebDriver driver) {
        get(driver);
        login(driver);
        reset(driver);
    }

    public static void clickSave(WebDriver driver) {
        TestUtils.jsClick(driver, driver.findElement(By.id("pa-entity-form-save-btn")));
    }

    public static void clickSaveDraft(WebDriver driver) {
        TestUtils.jsClick(driver, driver.findElement(By.id("pa-entity-form-draft-btn")));
    }

    public static void clickCancel(WebDriver driver) {
        TestUtils.jsClick(driver, driver.findElement(By.xpath("//button[text()='Cancel']")));
    }

    public static void clickCreateRecord(WebDriver driver) {
        driver.findElement(By.xpath("//i[text()='create_new_folder']")).click();
    }

    public static void clickRecycleBin(WebDriver driver) {
        driver.findElement(By.xpath("//i[text()='delete_outline']")).click();
    }
}
