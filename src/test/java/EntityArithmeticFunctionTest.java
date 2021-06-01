import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ProjectUtils;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.TestUtils;

import java.util.List;

public class EntityArithmeticFunctionTest extends BaseTest {

    private static final By F1_FIELD = By.xpath("//input[@id='f1']");
    private static final By F2_FIELD = By.xpath("//input[@id='f2']");
    private static final By DIV_FIELD = By.xpath("//input[@id='div']");
    private static final By CREATE_NEW = By.xpath("//i[text()='create_new_folder']");


    @Test
    public void testRecordSaveDraft(){

        final Integer F1 = 10;
        final Integer F2 = 2;
        final Integer DIV = F1 / F2;
        final List<Integer> expectedList = List.of(F1, F2, F1 + F2, F1 - F2, F1 * F2, DIV);

        ProjectUtils.start(getDriver());

        TestUtils.scrollClick(getDriver(),
                findElement(By.xpath("//p[contains(text(),'Arithmetic Function')]")));

        findElement(CREATE_NEW).click();

        findElement(F1_FIELD).sendKeys(F1.toString());
        findElement(F2_FIELD).sendKeys(F2.toString());

        getWait().until(ExpectedConditions.attributeToBe(DIV_FIELD,"value", DIV.toString()));

        findElement(By.xpath("//button[@id='pa-entity-form-draft-btn']")).click();

        WebElement icon = findElement(By.xpath("//tbody/tr/td[1]/i"));
        List<WebElement> columnList = findElements(By.xpath("//tbody/tr/td[@class = 'pa-list-table-th']"));

        Assert.assertEquals(icon.getAttribute("class"), "fa fa-pencil");
        Assert.assertEquals(columnList.size(), expectedList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            Assert.assertEquals(columnList.get(i).getText(), expectedList.get(i).toString());
        }
    }

    private void sendKeysSlow(WebElement element, String text) {
        if (!element.getAttribute("value").isEmpty()) {
            element.clear();
        }
        String newValue = "";

        for (char ch: text.toCharArray()) {
            element.sendKeys(String.valueOf(ch));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                }

            newValue += ch;
            getWait().until(ExpectedConditions.attributeToBe(element, "value", newValue));
        }
    }

    private void fillForm(Integer inputField1, Integer inputField2) {
        WebElement f1 = findElement(F1_FIELD);
        WebElement f2 = findElement(F2_FIELD);
        String expectedValueDIV = String.valueOf(inputField1/inputField2);

        sendKeysSlow(f1, inputField1.toString());
        sendKeysSlow(f2, inputField2.toString());

        getWait().until(ExpectedConditions.attributeToBe(DIV_FIELD, "value", expectedValueDIV));
        findElement(By.xpath("//button[@id='pa-entity-form-save-btn']")).click();
    }

    private void createRecord() {
        TestUtils.scrollClick(getDriver(),
                By.xpath("//p[text()=' Arithmetic Function ']/parent::a/parent::li"));
        findElement(CREATE_NEW).click();

        fillForm(20, 10);
    }

    @Test
    public void testEditRecord() {
        final Integer F1 = 40;
        final Integer F2 = 10;
        final List<Integer> expectedList = List.of(F1, F2, F1 + F2, F1 - F2, F1 * F2, F1 / F2);

        ProjectUtils.start(getDriver());

        createRecord();

        findElement(By.xpath("//div[@class='dropdown pull-left']")).click();
        getWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='view']")));
        findElement(By.xpath("//a[text()='edit']")).click();
        fillForm(F1, F2);

        List<WebElement> columnList = findElements(By.xpath("//tbody/tr/td[@class = 'pa-list-table-th']"));

        Assert.assertEquals(columnList.size(), expectedList.size());
        for (int i = 0; i < expectedList.size(); i++) {
            Assert.assertEquals(columnList.get(i).getText(), expectedList.get(i).toString());
        }
    }
}
