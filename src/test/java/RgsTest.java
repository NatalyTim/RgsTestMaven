import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class RgsTest extends BaseTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"IVAN NIKOLAEV", "15.02.1966", true, false},
                {"SVETA LI", "11.12.1991", false, false},
                {"PETR PETROV", "15.02.1966", true, true}});
    }

    @Parameterized.Parameter
    public String fullName;

    @Parameterized.Parameter(1)
    public String birthDay;

    @Parameterized.Parameter(2)
    public boolean needActiveSport;

    @Parameterized.Parameter(3)
    public boolean lastParameter;


    @BeforeClass
    public static void openForm() throws Exception {
        //2. Выбрать пункт меню - Страховани
        System.out.println("2. Выбрать пункт меню - Страховани");
        clickElement(By.xpath("//ol/li/a[contains(text(),'Страхование')]"));

        //3. Путешествия – Страхование выезжающих за рубеж
        System.out.println("3. Путешествия – Страхование выезжающих за рубеж");
        clickElement(By.xpath("//a[contains(text(),'Выезжающим за рубеж')]"));

        //4. Нажать рассчитать – Онлайн
        System.out.println("4. Нажать рассчитать – Онлайн");
        scrollToAndClickElement(By.xpath("//*[contains(text(),'Рассчитать')][contains(@class,'btn-attention')]"));

        //5. Проверить наличие заголовка – Страхование выезжающих за рубеж
        System.out.println("5. Проверить наличие заголовка – Страхование выезжающих за рубеж");
        String expectString = "Страхование выезжающих за рубеж";
        compareText("//div/span[@class='h1']", expectString);

    }

    @Test
    public void rgs() throws Exception {
        if (lastParameter) {
            //5.5. Я согласен на обработку данных  - выбрать чекбокс
            System.out.println("5.5 Я согласен на обработку данных  - выбрать чекбокс");
            scrollToAndClickElement(By.xpath("//form[contains(@data-bind,'calculation')]" +
                    "/div[contains(@data-bind,'validationApply')]/adaptive-checkbox/label"));
        }

        //6. Заполнить форму: Несколько поездок в течении года
        System.out.println("6. Заполнить форму: Несколько поездок в течении года");
        scrollToAndClickElement(By.xpath("//*[contains(text(),'Несколько')]"));

        //7.Куда едем – Шенген
        System.out.println("7.Куда едем – Шенген");
        WebElement element = driver.findElement(By.id("Countries"));
        scrollToElement(element);
        element.sendKeys("Шен");
        element.sendKeys(Keys.TAB);

        // 8.Страна въезда – Испания
        System.out.println("8.Страна въезда – Испания");
        for(int i = 0 ; i < 10; i++) {
            try {
                element = driver.findElement(By.name("ArrivalCountryList"));
                scrollToElement(element);
                new Select(element).selectByVisibleText("Испания");
                break;
            }catch(Exception ex){
                if(i>= 9)
                    throw new Exception("Невозможно найти элемент!");
                continue;
            }
        }
        // 9.Дата первой поездки – 1 ноября
        System.out.println("9.Дата первой поездки");
        scrollToAndClickElement(By.xpath("//input[contains(@data-bind, 'FirstDepartureDate')]"));
        WebElement nextMonth = driver.findElement(By.xpath("//div[contains(@class, 'datepicker-days')]" +
                "/table/thead/tr/th[contains(@class, 'next')]"));
        setFirstDateOfTrip(nextMonth);

        // 10.Сколько дней планируете пробыть за рубежом за год – не более 90
        System.out.println("10.Сколько дней планируете пробыть за рубежом за год – не более 90");
        scrollToAndClickElement(By.xpath("//*[contains(@data-bind,'btnRadioGroupValue: 90')]"));

        //11. ФИО
        System.out.println("11.ФИО");
        element = driver.findElement(By.xpath("//input[contains(@class,'form-control')][@data-test-name='FullName']"));
        scrollToElement(element);
        element.click();
        element.clear();
        element.sendKeys(fullName);

        //12.Дата рождения
        System.out.println("12.Дата рождения");
        for (int i = 0; i < 10; i++) {
            try {
                element = (new WebDriverWait(driver, 10)
                        .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-test-name='BirthDate']"))));
                scrollToElement(element);
                printElement(element);
                element.click();
                element.clear();
                element.sendKeys(birthDay);
                element.sendKeys(Keys.TAB);
                break;
            }catch (Exception e){
                if(i>= 9)
                    throw new Exception("Невозможно найти элемент!");
                continue;
            }
        }

        //13.Планируется активный отдых
        System.out.println("13.Планируется активный отдых");
        scrollToAndClickActiveSport(By.xpath("//div[contains(@data-bind,'activeRestOrSportsToggle')]/div[contains(@class,  'toggle-rgs')]"), needActiveSport);

        if (lastParameter) {
            //15. Нажать рассчитать
            System.out.println("15. Нажать рассчитать");
            scrollToAndClickElement(By.xpath("//*[@data-test-name='NextButton'][contains(@data-bind,'Misc.NextButton')]"));

            //16. Проверить значения:
            System.out.println("16.Проверить значения");

            //16.0 Условия страхования – Многократные поездки в течении года
            System.out.println("16.0 Условия страхования – Многократные поездки в течении года");
            String trips = "Многократные поездки в течение года";
            compareText("//span[contains(@class,'summary-value')][@data-bind='with: Trips']/span[@class='text-bold']", trips);

            //16.1 Территория – Шенген
            System.out.println("16.1 Территория – Шенген");
            compareText("//span/span[contains(@data-bind,'foreach: countries')]/strong", "Шенген");

            //16.2 Застрахованный
            System.out.println("16.2 Застрахованный");
            compareText("//strong[contains(@data-bind,'LastName')]", fullName);

            //16.3 Дата рождения
            System.out.println("16.3 Дата рождения");
            compareText("//strong[contains(@data-bind,' text: BirthDay.')]", birthDay);

            //16.4 Активный отдых - включен
            System.out.println("16.4 Активный отдых - включен");
            compareText("//div[contains(@data-bind, 'SelectedProgram.Options')]" +
                    "/div[contains(@data-bind, 'Активный')]/div[@class='summary-row']" +
                    "/span[@class='summary-value']/span", "Включен");
        }
    }
}
