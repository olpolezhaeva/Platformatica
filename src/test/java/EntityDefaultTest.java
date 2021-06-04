import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import utils.ProjectUtils;
import utils.TestUtils;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static utils.ProjectUtils.start;
import static utils.TestUtils.jsClick;
import static utils.TestUtils.scrollClick;

@Ignore
public class EntityDefaultTest extends BaseTest {

    private static final By DEFAULT_TAB = By.xpath("//p[contains (text(), 'Default')]");
    private static final By CREATE_NEW_RECORD = By.xpath("//div/i[.='create_new_folder']");
    private static final By STRING_FIELD = By.id("string");
    private static final By TEXT_FIELD = By.id("text");
    private static final By INT_FIELD = By.id("int");
    private static final By DECIMAL_FIELD = By.id("decimal");
    private static final By DATE_FIELD = By.id("date");
    private static final By DATETIME_FIELD = By.id("datetime");
    private static final By TESTER_NAME_FIELD = By.xpath("//div[@class = 'filter-option-inner']");
    private static final By TESTER_NAME = By.xpath("//span[text()='tester100@tester.test']");
    private static final By TESTER_NAME2 = By.xpath("//span[text()='tester88@tester.test']");
    private static final By SAVE_BUTTON = By.id("pa-entity-form-save-btn");
    private static final By CHECK_ICON = By.xpath("//tbody/tr[1]/td[1]/i[1]");
    private static final By COLUMN_FIELD = By.xpath("//tbody/tr/td[@class = 'pa-list-table-th']");
    private static final By ACTIONS_BUTTON = By.xpath("//i[text()='menu']");
    private static final By VIEW_OPTION = By.xpath("//a[@href] [contains(text(), 'view')]");
    private static final By LIST_OF_RECORDS = By.xpath("//span [@class = 'pa-view-field']");
    private static final By USER_FIELD = By.xpath("//div [@class = 'form-group']/p");
    private static final By EXIT_BUTTON = By.xpath("//i[contains(text(),'clear')]");
    private static final By EDIT_OPTION = By.xpath("//a[@href] [contains(text(), 'edit')]");
    private static final By DELETE_OPTION = By.xpath("//a[@href] [contains(text(), 'delete')]");
    private static final By RECYCLE_BIN = By.xpath("//i[contains(text(),'delete_outline')]");
    private static final By RECYCLE_INFO = By.xpath("//span[@class='pagination-info']");
    private static final By LIST_BUTTON = By
            .xpath("//a[@href=\"index.php?action=action_list&list_type=table&entity_id=7\"]");
    private static final By ORDER_BUTTON = By
            .xpath("//a[@href=\"index.php?action=action_list&list_type=table&entity_id=7&draggable=1\"]");

    private final String stringInputValue = "String";
    private final String textInputValue = "Text";
    private final String intInputValue = "2021";
    private final String decimalInputValue = "0.10";
    private final String emptyField = "";
    private final String userName = "tester100@tester.test";

    private final String stringInputValue2 = "String2";
    private final String textInputValue2 = "Text2";
    private final String intInputValue2 = "2022";
    private final String decimalInputValue2 = "0.20";
    private final String userName2 = "tester88@tester.test";

    private final Date date = new Date();
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    private final List<Object> expectedValues = Arrays.asList(
            stringInputValue, textInputValue, intInputValue, decimalInputValue,
            formatter.format(date), emptyField, emptyField, emptyField, userName);

    private void createRecord() {
        start(getDriver());

        scrollClick(getDriver(), findElement(DEFAULT_TAB));

        findElement(CREATE_NEW_RECORD).click();
        findElement(STRING_FIELD).clear();
        findElement(TEXT_FIELD).clear();
        findElement(INT_FIELD).clear();
        findElement(DECIMAL_FIELD).clear();
        findElement(DATE_FIELD).clear();
        findElement(DATETIME_FIELD).clear();
        findElement(STRING_FIELD).sendKeys(stringInputValue);
        findElement(TEXT_FIELD).sendKeys(textInputValue);
        findElement(INT_FIELD).sendKeys(intInputValue);
        findElement(DECIMAL_FIELD).sendKeys(decimalInputValue);
        findElement(DATE_FIELD).click();
        findElement(TESTER_NAME_FIELD).click();

        jsClick(getDriver(), findElement(TESTER_NAME));
    }

    private void clickListButton(){
        getWait().until(ExpectedConditions.elementToBeClickable(getDriver()
                .findElement(LIST_BUTTON)))
                .click();
    }

    private void clickOrderButton(){
        getWait().until(ExpectedConditions.elementToBeClickable(getDriver()
                .findElement(ORDER_BUTTON)))
                .click();
    }

    private void getAssertion(){
        List<WebElement> columnList = findElements(COLUMN_FIELD);

        Assert.assertEquals(columnList.size(), expectedValues.size());
        for (int i = 0; i < expectedValues.size(); i++) {
            Assert.assertEquals(columnList.get(i).getText(), expectedValues.get(i).toString());
        }
    }

    private void secondStart() {
        ProjectUtils.get(getDriver());
        ProjectUtils.login(getDriver());
        scrollClick(getDriver(), findElement(DEFAULT_TAB));
    }

    @Test
    public void testCreateRecord() {

        createRecord();
        jsClick(getDriver(), findElement(SAVE_BUTTON));

        WebElement icon1 = findElement(CHECK_ICON);
        Assert.assertEquals(icon1.getAttribute("class"), "fa fa-check-square-o");
        getAssertion();
    }

    @Ignore
    @Test(dependsOnMethods = "testCreateRecord")
    public void testViewRecord() {
        secondStart();

        List<Object> expectedRecords = Arrays.asList(stringInputValue, textInputValue, intInputValue, decimalInputValue,
                formatter.format(date), emptyField);

        findElement(ACTIONS_BUTTON).click();

        getWait().until(TestUtils.movingIsFinished(getDriver().findElement(VIEW_OPTION))).click();

        List<WebElement> actualRecords = findElements(LIST_OF_RECORDS);

        for (int i = 0; i < expectedRecords.size() ; i++) {
            Assert.assertEquals(actualRecords.get(i).getText(), expectedRecords.get(i).toString());
        }
        Assert.assertEquals(findElement(USER_FIELD).getText(), userName);

        findElement(EXIT_BUTTON).click();
    }
    @Ignore
    @Test(dependsOnMethods = "testCreateRecord")
    public void testSwitchBetweenListAndOrder(){
        secondStart();

        clickOrderButton();
        getAssertion();

        clickListButton();
        getAssertion();

        findElement(ACTIONS_BUTTON).click();

        Assert.assertEquals(findElement(VIEW_OPTION).getText(), "view");
        Assert.assertEquals(findElement(EDIT_OPTION).getText(), "edit");
        Assert.assertEquals(findElement(DELETE_OPTION).getText(), "delete");
    }

    @Test(dependsOnMethods = "testCreateRecord")
    public void testEditRecord() {
        secondStart();

        findElement(ACTIONS_BUTTON).click();

        getWait().until(TestUtils.movingIsFinished(getDriver().findElement(EDIT_OPTION))).click();

        findElement(STRING_FIELD).clear();
        findElement(TEXT_FIELD).clear();
        findElement(INT_FIELD).clear();
        findElement(DECIMAL_FIELD).clear();
        findElement(DATE_FIELD).clear();
        findElement(DATETIME_FIELD).clear();
        findElement(STRING_FIELD).sendKeys(stringInputValue2);
        findElement(TEXT_FIELD).sendKeys(textInputValue2);
        findElement(INT_FIELD).sendKeys(intInputValue2);
        findElement(DECIMAL_FIELD).sendKeys(decimalInputValue2);
        getWait().until(ExpectedConditions.elementToBeClickable(findElement(DATE_FIELD)));
        findElement(DATE_FIELD).click();
        findElement(TESTER_NAME_FIELD).click();

        jsClick(getDriver(), findElement(TESTER_NAME2));

        jsClick(getDriver(), findElement(SAVE_BUTTON));

        List<WebElement> columnList = findElements(COLUMN_FIELD);

        List<Object> expectedRecords = Arrays.asList(stringInputValue2, textInputValue2, intInputValue2, decimalInputValue2,
                formatter.format(date), emptyField, emptyField, emptyField, userName2);

        Assert.assertEquals(columnList.size(), expectedRecords.size());
        for (int i = 0; i < expectedRecords.size(); i++) {
            Assert.assertEquals(columnList.get(i).getText(), expectedRecords.get(i).toString());
        }
    }

    @Test(dependsOnMethods = "testEditRecord")
    public void testDeleteRecord() {
        secondStart();

        findElement(ACTIONS_BUTTON).click();
        getWait().until(TestUtils.movingIsFinished(getDriver().findElement(DELETE_OPTION))).click();

        findElement(RECYCLE_BIN).click();
        WebElement recycleBinPage = findElement(RECYCLE_INFO);
        String currentString = recycleBinPage.getText();
        boolean checkBin = !currentString.equals("");

        Assert.assertTrue(checkBin, "Showing 1 to 1 of 1 rows");
    }

    @Test(dependsOnMethods = "testDeleteRecord")
    public void testRestoreRecord() {
        secondStart();

        findElement(RECYCLE_BIN).click();
        findElement(By.linkText("restore as draft")).click();

        Assert.assertEquals(findElement(By.className("card-body")).getText(), "Good job with housekeeping! Recycle bin is currently empty!");
        findElement(DEFAULT_TAB).click();

        WebElement icon = findElement(By.xpath("//tbody/tr/td[1]/i"));

        Assert.assertEquals(icon.getAttribute("class"), "fa fa-pencil");

        String result = findElement(By.xpath("//table[@id='pa-all-entities-table']/tbody")).getText();
        Assert.assertFalse(result.isEmpty());
    }

    @Test
    public void testCreateNewRecordAsDraft() {
        ProjectUtils.start(getDriver());

        WebElement EntityDefault = findElement(By.xpath("//a[@href='index.php?action=action_list&entity_id=7&mod=2']"));
        EntityDefault.click();

        WebElement NewRecord = findElement(By.xpath("//i[contains(text(),'create_new_folder')]"));
        NewRecord.click();

        String string = findElement(By.id("string")).getAttribute("value");
        String text = findElement(By.id("text")).getText();
        String intField = findElement(By.id("int")).getAttribute("value");
        String decimal = findElement(By.id("decimal")).getAttribute("value");
        String datetime = findElement(By.id("datetime")).getAttribute("value");
        String pencilIconClass = "fa fa-pencil";

        WebElement SaveDraft = findElement(By.id("pa-entity-form-draft-btn"));
        SaveDraft.click();

        WebElement icon = findElement(By.xpath("//tbody/tr/td[1]/i"));

        String result = findElement(By.xpath("//table[@id='pa-all-entities-table']/tbody")).getText();

        Assert.assertTrue(result.contains(string),(text));
        Assert.assertTrue(result.contains(intField),(decimal));
        Assert.assertTrue(result.contains(datetime),(text));
        Assert.assertEquals(icon.getAttribute("class"), pencilIconClass);
    }
}