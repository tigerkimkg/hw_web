package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldOrderSuccess() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldWarningMessageIfNotValidName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Petrov Ivan");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }

    @Test
    void shouldWarningMessageIfEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).isDisplayed());
    }

    @Test
    void shouldWarningMessageIfNotValidPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7999123456");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }

    @Test
    void shouldWarningMessageIfEmptyPhone() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).isDisplayed());
    }

    @Test
    void shouldWarningMessageIfNotClickCheckbox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Петров Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("button")).click();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и " +
                        "разрешаю сделать запрос в бюро кредитных историй",
                driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).getText().trim());
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).isDisplayed());
    }
}
