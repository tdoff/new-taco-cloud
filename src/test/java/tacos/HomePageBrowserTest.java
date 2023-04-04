package tacos;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HomePageBrowserTest {

    @LocalServerPort
    private int port;

    private static HtmlUnitDriver browser;

    @BeforeAll
    public static void setup() {
        browser = new HtmlUnitDriver();

        browser.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10L));
    }

    @AfterAll
    public static void teardown() {
        browser.quit();
    }

    @Test
    public void testHomePage() {
        String homePage = "http://localhost:" + port;
        browser.get(homePage);

        String titleText = browser.getTitle();
        assertEquals("Taco CLoud", titleText);

        String h1Text = browser.findElement(By.tagName("h1")).getText();
        assertEquals("Welcome to...", h1Text);

        String imgSrc = browser.findElement(By.tagName("img")).getAttribute("src");
        assertEquals(homePage + "/images/TacoCloud.png", imgSrc);
    }

}
