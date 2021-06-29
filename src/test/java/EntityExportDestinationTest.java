import base.BaseTest;
import model.ExportDestinationPage;
import model.ExportDestinationViewPage;
import model.MainPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class EntityExportDestinationTest extends BaseTest {

    private static final String STRING_VALUE = "Some string";
    private static final String TEXT_VALUE = "Export destination text.";
    private static final String INT_VALUE = "457";
    private static final String DECIMAL_VALUE = "27.35";
    private static final String DATE_VALUE = "01/06/2021";
    private static final String DATETIME_VALUE = "01/06/2021 13:07:06";
    private static final String USERNAME_VALUE = "apptester1@tester.test";
    private static final String FILE_VALUE = "";
    private static final String CHECK_SQUARE_ICON = "fa fa-check-square-o";

    private final static List<String> EXPECTED_VALUES = Arrays.asList(
            STRING_VALUE, TEXT_VALUE, INT_VALUE, DECIMAL_VALUE,
            DATE_VALUE, DATETIME_VALUE, FILE_VALUE, USERNAME_VALUE);

    @Test
    public void testCreateRecord() {

        ExportDestinationPage exportDestinationPage = new MainPage(getDriver())
                .clickExportDestinationMenu()
                .clickNewButton()
                .fillString(STRING_VALUE)
                .fillText(TEXT_VALUE)
                .fillInt(INT_VALUE)
                .fillDecimal(DECIMAL_VALUE)
                .fillDate(DATE_VALUE)
                .fillDateTime(DATETIME_VALUE)
                .clickSave();

        Assert.assertEquals(exportDestinationPage.getClassIcon(), CHECK_SQUARE_ICON);
        Assert.assertEquals(exportDestinationPage.getRow(0), EXPECTED_VALUES);
    }

    @Test(dependsOnMethods = "testCreateRecord")
    public void testViewRecord() {

        ExportDestinationViewPage exportDestinationViewPage = new MainPage(getDriver())
                .clickExportDestinationMenu()
                .clickActions()
                .clickActionsView();

        Assert.assertEquals(exportDestinationViewPage.getRecordInViewMode(), EXPECTED_VALUES);
    }
}
