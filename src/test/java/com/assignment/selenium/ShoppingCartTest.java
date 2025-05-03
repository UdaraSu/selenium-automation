package com.assignment.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

public class ShoppingCartTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://demoblaze.com");
    }

    @Test
    void testAddItemToCart() throws InterruptedException {
        login("udara@gmail.com", "udara123");

        navigateToProduct("Samsung galaxy s6");

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        Thread.sleep(1000);
        driver.findElement(By.id("cartur")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'Samsung galaxy s6')]")));
        Assertions.assertTrue(driver.getPageSource().contains("Samsung galaxy s6"));
    }

    @Test
    void testRemoveItemFromCart() throws InterruptedException {
        login("udara@gmail.com", "udara123");

        navigateToProduct("Samsung galaxy s6");

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        Thread.sleep(1000);
        driver.findElement(By.id("cartur")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'Samsung galaxy s6')]")));
        driver.findElement(By.linkText("Delete")).click();

        Thread.sleep(2000);
        Assertions.assertFalse(driver.getPageSource().contains("Samsung galaxy s6"));
    }

    @Test
    void testCartPersistenceAfterLogoutLogin() throws InterruptedException {
        login("udara@gmail.com", "udara123");

        navigateToProduct("Samsung galaxy s6");

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        Thread.sleep(1000);
        logout();

        login("udara@gmail.com", "udara123");

        driver.findElement(By.id("cartur")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'Samsung galaxy s6')]")));
        Assertions.assertTrue(driver.getPageSource().contains("Samsung galaxy s6"));
    }

    @Test
    void testAddSameProductMultipleTimes() throws InterruptedException {
        login("udara@gmail.com", "udara123");

        for (int i = 0; i < 2; i++) {
            navigateToProduct("Samsung galaxy s6");

            wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();

            Thread.sleep(1000);
        }

        driver.findElement(By.id("cartur")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'Samsung galaxy s6')]")));
        List<WebElement> items = driver.findElements(By.xpath("//td[contains(text(), 'Samsung galaxy s6')]"));

        Assertions.assertTrue(items.size() >= 2);
    }

    void login(String username, String password) {
        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername"))).sendKeys(username);
        driver.findElement(By.id("loginpassword")).sendKeys(password);
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
    }

    void logout() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout2"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login2")));
    }

    void navigateToProduct(String productName) {
        // Click the brand logo to go to home
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.navbar-brand"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(productName))).click();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
