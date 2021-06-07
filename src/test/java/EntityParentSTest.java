import base.DriverPerClassBaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ProjectUtils;
import utils.TestUtils;

import java.util.List;

public class EntityParentSTest extends DriverPerClassBaseTest {

    private void createRecord() {

        ProjectUtils.clickCreateRecord(getDriver());
        findElement(By.id("string")).sendKeys("Hello world");
        findElement(By.id("text")).sendKeys("Be healthy");
        findElement(By.id("int")).sendKeys("123");
        findElement(By.id("decimal")).sendKeys("456.98");

        TestUtils.jsClick(getDriver(), getDriver().findElement(By.xpath("//button[@data-id='user']")));
        TestUtils.jsClick(getDriver(), getDriver().findElement(
                By.xpath("//span[text()='tester26@tester.test']")));
        ProjectUtils.clickSave(getDriver());
    }

    private void editRecord() {

        getDriver().findElement(By.id("string")).clear();
        getDriver().findElement(By.id("string")).sendKeys("Hello for everyone");

        getDriver().findElement(By.id("text")).clear();
        getDriver().findElement(By.id("text")).sendKeys("Peace to all");

        getDriver().findElement(By.id("int")).clear();
        getDriver().findElement(By.id("int")).sendKeys("345");

        getDriver().findElement(By.id("decimal")).clear();
        getDriver().findElement(By.id("decimal")).sendKeys("345.67");

        TestUtils.jsClick(getDriver(), getDriver().findElement(By.xpath("//button[@data-id='user']")));
        TestUtils.jsClick(getDriver(), getDriver().findElement(
                By.xpath("//span[text()='tester26@tester.test']")));
        ProjectUtils.clickSave(getDriver());
    }

    private void clickParentButton() {
        TestUtils.jsClick(getDriver(), getDriver().findElement(By.xpath("//p[text()=' Parent ']")));
    }

    private void clickListButton() {
        findElement(By.xpath(
                "//a[@href='index.php?action=action_list&list_type=table&entity_id=57']")).click();
    }

    private final List<String> world = List.of(
            "Hello world", "Be healthy", "123", "456.98", "", "");

    private final List<String> everyone = List.of(
            "Hello for everyone", "Peace to all", "345", "345.67", "", "");

    @Test
    public void testCreateRecord() {

        ProjectUtils.start(getDriver());
        clickParentButton();
        createRecord();

        WebElement icon = findElement(By.xpath("//tbody/tr/td/i"));

        List<WebElement> result = getDriver().findElements(By.xpath(
                "//tbody/tr/td/a"));

        Assert.assertEquals(icon.getAttribute("class"), "fa fa-check-square-o");
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(result.get(i).getText(), world.get(i));
        }
    }

    @Test(dependsOnMethods = "testCreateRecord")
    public void testViewRecord() {

        clickListButton();

        findElement(By.xpath("//button/i[@class='material-icons']")).click();
        TestUtils.jsClick(getDriver(), getDriver().findElement(By.xpath("//a[text()='view']")));

        List<WebElement> row = findElements(By.xpath("//span[@class='pa-view-field']"));
        Assert.assertEquals(row.size(), 6);
        for (int i = 0; i < row.size(); i++) {
            Assert.assertEquals(row.get(i).getText(), world.get(i));
        }
        getDriver().findElement(By.xpath("//i[text()='clear']")).click();
    }

    @Test(dependsOnMethods = "testViewRecord")
    public void testEditRecord() {

        WebElement record = getDriver().findElement(By.tagName("tbody"));
        record.getText();

        findElement(By.xpath("//button/i[@class='material-icons']")).click();
        TestUtils.jsClick(getDriver(), getDriver().findElement(By.xpath("//a[text()='edit']")));

        editRecord();

        List<WebElement> result = getDriver().findElements(By.xpath("//tbody/tr/td/a"));
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(result.get(i).getText(), everyone.get(i));
        }

        WebElement newRecord = findElement(By.tagName("tbody"));
        newRecord.getText();
        Assert.assertNotEquals(record, newRecord);
    }

    @Test(dependsOnMethods = "testEditRecord")
    public void testSearchRecord() {

        createRecord();

        WebElement record = getDriver().findElement(By.xpath("//tr[@data-index='0']"));
        record.getText();
        WebElement record1 = getDriver().findElement(By.xpath("//tr[@data-index='1']"));
        record1.getText();

        List<WebElement> fields = findElements(By.xpath("//tbody/tr"));
        Assert.assertEquals(fields.size(), 2);
        Assert.assertNotEquals(record, record1);

        findElement(By.xpath("//input[@type='text']")).sendKeys("world");
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//span[@class='pagination-info']"), "Showing 1 to 1 of 1 rows"));

        List<WebElement> result = getDriver().findElements(By.xpath("//tbody/tr/td/a"));
        for (int i = 0; i < result.size(); i++) {
            Assert.assertEquals(result.get(i).getText(), world.get(i));
        }

        findElement(By.xpath("//input[@type='text']")).clear();
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//span[@class='pagination-info']"), "Showing 1 to 2 of 2 rows"));

        findElement(By.xpath("//input[@type='text']")).sendKeys("for");
        getWait().until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//span[@class='pagination-info']"), "Showing 1 to 1 of 1 rows"));

        List<WebElement> result1 = getDriver().findElements(By.xpath("//tbody/tr/td/a"));
        for (int i = 0; i < result1.size(); i++) {
            Assert.assertEquals(result1.get(i).getText(), everyone.get(i));
        }
    }

    @Test(dependsOnMethods = "testSearchRecord")
    public void testReorderRecord() {


        findElement(By.xpath("//input[@type='text']")).clear();
        findElement(By.xpath("//i[text()='format_line_spacing']")).click();

        List<WebElement> record = getDriver().findElements(By.xpath("//tbody/tr/td/a"));
        Assert.assertEquals(record.get(0).getText(), everyone.get(0));

        Actions actions = new Actions(getDriver());
        WebElement row = findElement(By.xpath("//tbody/tr"));
        actions.moveToElement(row).clickAndHold(row).dragAndDropBy(row, 0, 20);
        Action swapRow = actions.build();
        swapRow.perform();

        List<WebElement> record1 = getDriver().findElements(By.xpath("//tbody/tr/td/a"));
        Assert.assertEquals(record1.get(0).getText(), world.get(0));

        getDriver().findElement(By.xpath("//i[@class='fa fa-toggle-off']")).click();

        List<WebElement> viewToggle = getDriver().findElements(By.xpath("//div/span/a"));
        Assert.assertEquals(viewToggle.get(0).getText(), world.get(0));

        WebElement card = getDriver().findElement(By.id("customId_0"));
        actions.moveToElement(card).clickAndHold(card).dragAndDropBy(card, 0, 100);
        Action swapCard = actions.build();
        swapCard.perform();

        List<WebElement> viewCard = getDriver().findElements(By.xpath("//div/span/a"));
        Assert.assertEquals(viewCard.get(0).getText(), everyone.get(0));
    }
}
