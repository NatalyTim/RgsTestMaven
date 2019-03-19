
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;

public class BaseTest {
    public static WebDriver driver;
    public static String browser = "chrome";
     @BeforeClass
    public static void setUp() {
         browser = System.getProperty("driverType");
         System.out.println("browser = " + browser);
        String url = "https://www.rgs.ru/";
        if ("firefox".equals(browser)) {
            System.out.println(" FireFox");
            System.setProperty("webdriver.gecko.driver", "drv/geckodriver.exe");
            driver = new FirefoxDriver();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            browser = "firefox";
            driver.get(url);
        }else {
            System.out.println(" Chrome");
            System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            browser = "chrome";
            driver.get(url);
        }

    }

    @AfterClass
    public static void close() {
        driver.quit();
    }

    public static void compareText(String xpath, String expect) throws Exception {

        WebElement s1 = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        scrollToElement(s1);
        scrollToElement(s1);
       assertTrue("Исходного текста нет: ",
                expect.contains(driver.findElement(By.xpath(xpath)).getText()));
        System.out.println("Исходный текст есть:" + expect);

    }

    public static void printElement(WebElement element) {
        if (element == null) {
            System.out.println("Element is null");
        } else {
            System.out.println(element.getTagName() + " " + element.getText());
        }

    }

    public static void clickElement(By locator) {

        (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public static void setFirstDateOfTrip(WebElement nextMonth) {
        LocalDate dateNow = LocalDate.now();
        LocalDate dateInTwoWeeks = dateNow.plusDays(14);
        DateFormat df = new SimpleDateFormat();
        int reportDay = dateInTwoWeeks.getDayOfMonth();
        if (dateNow.getMonth() != dateInTwoWeeks.getMonth()) {
            nextMonth.click();
            driver.findElement(By.xpath("//div[contains(@class,'datepicker-days')]" +
                    "/table/tbody/tr/td[@class='day'][contains(text(), " + reportDay + ")]")).click();

        } else {
            driver.findElement(By.xpath("//div[contains(@class,'datepicker-days')]" +
                    "/table/tbody/tr/td[@class='day'][contains(text(), " + reportDay + ")]")).click();
        }
    }

    public static void scrollToAndClickElement(By locator) {

        WebElement element = (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(locator)));
        scrollToElement(element);
        scrollToElement(element);
        printElement(element);
        element.click();
    }

    public static void scrollToAndClickActiveSport(By locator, boolean bol) {

        WebElement element = (new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(locator)));
        scrollToElement(element);
        printElement(element);
        String classes = element.getAttribute("class");
        System.out.println("Classes = " + classes);
        System.out.println("bol = " + bol);
        if (classes.contains("off")) {
            if (bol) {
                element.click();
            }
        } else if (!classes.contains("off")) {
            if (!bol) {
                element.click();
            }
        }

    }

    public static void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    }

}
