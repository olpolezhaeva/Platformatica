import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import static utils.ProjectUtils.*;
import static utils.TestUtils.scrollClick;

public class EntityArithmeticFunction1Test extends BaseTest {

    private static final Integer F1 = 22;
    private static final Integer F2 = 2;
    private static final Integer SUM = F1 + F2;
    private static final Integer SUB = F1 - F2;
    private static final Integer MUL = F1 * F2;
    private static final Integer DIV = F1 / F2;
    private static final By DIV_FIELD = By.id("div");
    private static final List<Integer> expectedList = List.of(F1, F2, SUM, SUB, MUL, DIV);
    private static final By LINK_ENTITY_ARITHMETIC_FUNCTION = By.xpath("//p[normalize-space()='Arithmetic Function']");

    private void createNewRecords() {
        clickCreateRecord(getDriver());
        findElement(By.xpath("//input[@id='f1']")).sendKeys(F1.toString());
        findElement(By.xpath("//input[@id='f2']")).sendKeys(F2.toString());
        getWait().until(ExpectedConditions.attributeToBe(DIV_FIELD, "value", DIV.toString()));
        clickSave(getDriver());
    }

    @Test
    public void testCreateArithmeticFunction() {
        scrollClick(getDriver(), findElement(LINK_ENTITY_ARITHMETIC_FUNCTION));
        for (int i = 0; i < 12; i++) {
            createNewRecords();
        }
        List<WebElement> tableRecords = getDriver().findElements(By.xpath("//div[@ class = 'card-body ']//table/tbody/tr"));
        Assert.assertEquals(tableRecords.size(), 10);

        List<WebElement> elementsWeb = getDriver().findElements(By.xpath("//td[@class = 'pa-list-table-th']"));
        for (int i = 0; i < expectedList.size(); i++) {
            Assert.assertEquals(elementsWeb.get(i).getText(), expectedList.get(i).toString());
        }
    }
}
