import base.BaseTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.ProjectUtils.*;
import static utils.TestUtils.*;

public class EntityChevronTest extends BaseTest {

    final String ENTITY_NAME = "Chevron";
    final String PENDING = "Pending";
    final String FULFILLMENT = "Fulfillment";
    final String SENT = "Sent";

    private void chooseSideBarItem(String name){
        scrollClick(getDriver(), By.xpath("//a[@class='nav-link'][contains(.,'"+name+"')]"));
    }

    private void clickAddButton(){
        findElement(By.xpath("//i[contains(.,'create_new_folder')]")).click();
    }

    private void chooseStringDropDownItem(String dropDownItem) {
        WebElement stringButton = findElement(By.xpath("//button[@data-id='string']"));
        Assert.assertEquals(stringButton.getAttribute("title"), "Pending");

        stringButton.click();

        jsClick(getDriver(),
                findElement(By.xpath("//ul[@class='dropdown-menu inner show']//span[contains(.,'"+dropDownItem+"')]")));
        getWait().until(
                ExpectedConditions.invisibilityOf(findElement(By.xpath("//div[@class='dropdown-menu']"))));

        Assert.assertEquals(stringButton.getAttribute("title"), dropDownItem);
    }

    private List<String> makeRandomData(String dropDownItem){
        List<String> randomData = new ArrayList<>();

        randomData.add(dropDownItem);

        String formText = RandomStringUtils.randomAlphabetic(10);
        randomData.add(formText);
        randomData.add("" + RandomUtils.nextInt(0, 1000000));

        String formDecimal = RandomUtils.nextInt(0, 1000000) + "." + RandomStringUtils.randomNumeric(2);
        randomData.add(formDecimal);

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        randomData.add(date);

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        randomData.add(dateTime);

        randomData.add("");

        String user = "apptester1@tester.test";
        randomData.add(user);

        return randomData;
    }

    private void fillCreateNewFormFields(List<String> data){
        chooseStringDropDownItem(data.get(0));

        WebElement date = findElement(By.xpath("//input[@id='date']"));
        date.click();
        date.clear();
        date.sendKeys(data.get(4));

        WebElement datetime = findElement(By.xpath("//input[@id='datetime']"));
        datetime.click();
        datetime.clear();
        datetime.sendKeys(data.get(5));

        WebElement text = findElement(By.xpath("//textarea[@id='text']"));
        text.click();
        text.sendKeys(data.get(1));

        WebElement intData = findElement(By.xpath("//input[@id='int']"));
        intData.click();
        intData.sendKeys(data.get(2));

        WebElement decimalData = findElement(By.xpath("//input[@id='decimal']"));
        decimalData.click();
        decimalData.sendKeys(data.get(3));
    }

    private void clickSaveButton(String entity) {
        findElement(By.xpath("//button[@id='pa-entity-form-save-btn']")).click();
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@class='main-panel']//a/b"),
                entity));
    }

    private void clickSaveDraftButton(String entity) {
        findElement(By.xpath("//button[@type='submit'][text() ='Save draft']")).click();
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[@class='main-panel']//a/b"),
                entity));
    }

    private void checkCreatedRecord(String recordStatus, Boolean isDraft, List<String> data){
        if(recordStatus.equals(SENT)){
            findElement(By.xpath("//a[text()='All']")).click();
        }
        List<WebElement> records = findElements(By.xpath("//tbody/tr"));
        getWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tbody/tr")));
        List<WebElement> cells = findElements(By.xpath("//tbody/tr/td[@class = 'pa-list-table-th']"));

        if(isDraft){
            WebElement icon = findElement(By.xpath("//i[contains(@class,'fa fa-pencil')]"));
            Assert.assertEquals(icon.getAttribute("class"), "fa fa-pencil");
        }
        else {
            WebElement icon = findElement(By.xpath("//i[contains(@class,'check-square')]"));
            Assert.assertEquals(icon.getAttribute("class"), "fa fa-check-square-o");
        }

        for (int i = 0; i < data.size(); i++) {
            Assert.assertEquals(cells.get(i).getText(), data.get(i));
        }
    }
    
    @Test
    public void testCreatePendingRecord(){
        String status = PENDING;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickSaveButton(ENTITY_NAME);
        checkCreatedRecord(status, false, expectedData);
    }

    @Test
    public void testCreateFulfillmentRecord(){
        String status = FULFILLMENT;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickSaveButton(ENTITY_NAME);
        checkCreatedRecord(status, false, expectedData);
    }

    @Test
    public void testCreateSentRecord(){
        String status = SENT;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickSaveButton(ENTITY_NAME);
        checkCreatedRecord(status, false, expectedData);
    }

    @Test
    public void testCreatePendingDraftRecord(){
        String status = PENDING;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickSaveDraftButton(ENTITY_NAME);
        checkCreatedRecord(status, true, expectedData);
    }

    @Test
    public void testCreateFulfillmentDraftRecord(){
        String status = FULFILLMENT;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickSaveDraftButton(ENTITY_NAME);
        checkCreatedRecord(status, true, expectedData);
    }

    @Test
    public void testCreateSentDraftRecord(){
        String status = SENT;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickSaveDraftButton(ENTITY_NAME);
        checkCreatedRecord(status, true, expectedData);
    }

    @Test
    public void testCancelCreateRecord(){
        String status = PENDING;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickCancel(getDriver());
        Assert.assertEquals(findElements(By.cssSelector("tbody tr")).size(), 0);
    }

    @Test
    public void testCancelCreateDraftRecord(){
        String status = FULFILLMENT;

        start(getDriver());
        chooseSideBarItem(ENTITY_NAME);
        clickAddButton();
        List<String> expectedData = makeRandomData(status);
        fillCreateNewFormFields(expectedData);
        clickCancel(getDriver());
        Assert.assertEquals(findElements(By.cssSelector("tbody tr")).size(), 0);
    }
}
