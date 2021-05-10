import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;


public class GroupBreakingBadJavaTest {

    @BeforeClass
    public void before(){
        WebDriverManager.chromedriver().setup();
    }

    private WebDriver driver;
    @BeforeMethod
    public void beforeTest(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void dudeAlexSoccerTest() {
        driver.get("https://services.smartbear.com/samples/TestComplete14/smartstore/");
        driver.findElement(By.xpath("//*[@title='Soccer']")).click();

        WebElement elem = driver.findElement(By.id("content-center"))
                                .findElement(By.tagName("h1"));
        Assert.assertEquals(elem.getText(), "Soccer");
    }


    @Test
    public void evgeniyTMainGitCommandTest() {
        driver.get("https://use-web.ru/news.php?id=138&tid=3");
        WebElement result = driver.findElement(By.xpath("//h2[text()= 'Основные Git команды. ']"));

        Assert.assertEquals(result.getText(), "Основные Git команды.");
    }

    @AfterMethod
    public void afterTest() {
        driver.quit();
    }

}
