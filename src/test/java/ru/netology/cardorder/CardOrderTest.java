package ru.netology.cardorder;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardOrderTest {
    WebDriver driver;

    @BeforeAll
    static void setupALL() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }


    @Test
    public void shouldSubmitRequestHappyPath() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Голт");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79109101111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void shouldSubmitRequestHappyPathDoubleLastName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Голт-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79109101111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    public void shouldNotSubmitRequestInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Голт-Иванов1");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79109101111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldNotSubmitRequestInvalidNameInLatin() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("John Galt");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79109101111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    public void shouldNotSubmitRequestNoName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79109101111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void shouldNotSubmitRequestInvalidPhoneNumber() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Голт-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79109101111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    public void shouldNotSubmitRequestNoPhoneNumber() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Голт-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    public void shouldNotSubmitRequestCheckboxOff() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Джон Голт-Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79109101111");
        driver.findElement(By.cssSelector("[data-test-id='agreement']"));
        driver.findElement(By.tagName("button")).click();
        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__control"));
        Assertions.assertFalse(checkbox.isSelected());
    }
}
