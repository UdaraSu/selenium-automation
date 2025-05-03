package com.assignment.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class LoginTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://practicetestautomation.com/practice-test-login/");

        // Ensure screenshots directory exists
        new File("screenshots").mkdirs();
    }

    @Test
    void testValidLogin() throws IOException {
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("logged-in-successfully"));

        takeScreenshot("valid_login_passed.png");
    }

    @Test
    void testInvalidUsername() throws IOException {
        driver.findElement(By.id("username")).sendKeys("invalidUser");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

        System.out.println("Error Message: " + errorMessage.getText());
        Assertions.assertTrue(errorMessage.getText().contains("Your username is invalid!"));

        takeScreenshot("invalid_username.png");
    }

    @Test
    void testInvalidPassword() throws IOException {
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("wrongPass123");
        driver.findElement(By.id("submit")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

        System.out.println("Error Message: " + errorMessage.getText());
        Assertions.assertTrue(errorMessage.getText().contains("Your password is invalid!"));

        takeScreenshot("invalid_password.png");
    }

    @Test
    void testEmptyCredentials() throws IOException {
        driver.findElement(By.id("username")).sendKeys("");
        driver.findElement(By.id("password")).sendKeys("");
        driver.findElement(By.id("submit")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("error")));

        System.out.println("Error Message: " + errorMessage.getText());
        Assertions.assertTrue(errorMessage.getText().toLowerCase().contains("invalid"));

        takeScreenshot("empty_credentials.png");
    }

    @Test
    void testLoginButtonVisibility() throws IOException {
        WebElement submitButton = driver.findElement(By.id("submit"));

        Assertions.assertTrue(submitButton.isDisplayed());
        Assertions.assertTrue(submitButton.isEnabled());

        takeScreenshot("submit_button_check.png");
    }

    void takeScreenshot(String fileName) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File dest = new File("screenshots/" + fileName);
        FileUtils.copyFile(src, dest);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
