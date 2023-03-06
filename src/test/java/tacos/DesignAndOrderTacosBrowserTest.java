package tacos;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DesignAndOrderTacosBrowserTest {

    @LocalServerPort
    private int port;
    private static HtmlUnitDriver browser;

    @BeforeAll
    public static void setup() {
        browser = new HtmlUnitDriver();
        browser.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(10L));
    }

    @Test
    public void designTacoPage_HappyPath() {
        browser.get(homePageUrl());
        clickDesignTaco();
        assertDesignPageElements();
        buildAndSubmitTaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        clickBuildAnotherTaco();
        buildAndSubmitTaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");
        fillInAndSubmitOrderForm();
        assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
    }

    @Test
    public void designTacoPage_EmptyOrderInfo() {
        browser.get(homePageUrl());
        clickDesignTaco();
        assertDesignPageElements();
        buildAndSubmitTaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        submitEmptyOrderForm();
        fillInAndSubmitOrderForm();
        assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
    }

    @Test
    public void designTacoPage_InvalidOrderInfo() {
        browser.get(homePageUrl());
        clickDesignTaco();
        assertDesignPageElements();
        buildAndSubmitTaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        submitInvalidOrderForm();
        fillInAndSubmitOrderForm();
        assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
    }

    private void submitInvalidOrderForm() {
        assertThat(browser.getCurrentUrl()).isEqualTo(currentOrderDetailsPageUrl());
        fillField("input#deliveryName", "I");
        fillField("input#deliveryStreet", "1");
        fillField("input#deliveryCity", "F");
        fillField("input#deliveryState", "C");
        fillField("input#deliveryZip", "8");
        fillField("input#ccNumber", "1234432112344322");
        fillField("input#ccExpiration", "14/91");
        fillField("input#ccCVV", "1234");
        browser.findElement(By.cssSelector("form")).submit();

        assertThat(browser.getCurrentUrl()).isEqualTo(orderDetailsPageUrl());

        List<String> validationErrors = getValidationErrorTexts();
        assertThat(validationErrors.size()).isEqualTo(4);
        assertThat(validationErrors).containsExactlyInAnyOrder(
                "Please correct the problems below and resubmit.",
                "Not a valid card number",
                "Must be formatted MM/YY",
                "Invalid CVV"
        );
    }

    private void submitEmptyOrderForm() {
        assertThat(browser.getCurrentUrl()).isEqualTo(currentOrderDetailsPageUrl());
        browser.findElement(By.cssSelector("form")).submit();

        assertThat(browser.getCurrentUrl()).isEqualTo(orderDetailsPageUrl());

        List<String> validationErrors = getValidationErrorTexts();
        assertThat(validationErrors.size()).isEqualTo(9);
        assertThat(validationErrors).containsExactlyInAnyOrder(
                "Please correct the problems below and resubmit.",
                "Delivery name is required",
                "Street is required",
                "City is required",
                "State is required",
                "Zip code is required",
                "Not a valid card number",
                "Must be formatted MM/YY",
                "Invalid CVV"
        );
    }

    private static List<String> getValidationErrorTexts() {
        return browser.findElements(By.className("validationError")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private void fillInAndSubmitOrderForm() {
        assertThat(browser.getCurrentUrl()).startsWith(orderDetailsPageUrl());//'orders/current'
        fillField("input#deliveryName", "Ima Hungry");
        fillField("input#deliveryStreet", "1234 Culinary Blvd.");
        fillField("input#deliveryCity", "Foodsville");
        fillField("input#deliveryState", "CO");
        fillField("input#deliveryZip", "81019");
        fillField("input#ccNumber", "4111111111111111");
        fillField("input#ccExpiration", "10/24");
        fillField("input#ccCVV", "123");
        browser.findElement(By.cssSelector("form")).submit();
    }

    private void fillField(String fieldName, String value) {
        WebElement field = browser.findElement(By.cssSelector(fieldName));
        field.clear();
        field.sendKeys(value);
    }

    private void clickBuildAnotherTaco() {
        assertThat(browser.getCurrentUrl()).startsWith(orderDetailsPageUrl());//'orders/current'
        browser.findElement(By.cssSelector("a[id='another']")).click();
    }

    private String orderDetailsPageUrl() {
        return homePageUrl() + "orders";
    }

    private String currentOrderDetailsPageUrl() {
        return homePageUrl() + "orders/current";
    }

    private void clickDesignTaco() {
        assertThat(browser.getCurrentUrl()).isEqualTo(homePageUrl());
        browser.findElement(By.cssSelector("a[id='design']")).click();
    }

    private void buildAndSubmitTaco(String name, String... ingredients) {
        for (String ingredient : ingredients) {
            browser.findElement(By.cssSelector("input[value='" + ingredient + "']")).click();
        }
        browser.findElement(By.cssSelector("input#name")).sendKeys(name);
        browser.findElement(By.cssSelector("form")).submit();
    }

    private void assertDesignPageElements() {
        assertThat(browser.getCurrentUrl()).isEqualTo(designPageUrl());
        List<WebElement> ingredientGroups = browser.findElements(By.className("ingredient-group"));
        assertThat(ingredientGroups.size()).isEqualTo(5);

        WebElement wrapGroup = browser.findElement(By.cssSelector("div.ingredient-group#wraps"));
        List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
        assertThat(wraps.size()).isEqualTo(2);
        assertIngredient(wraps, 0, "FLTO", "Flour Tortilla");
        assertIngredient(wraps, 1, "COTO", "Corn Tortilla");

        WebElement proteinGroup = browser.findElement(By.cssSelector("div.ingredient-group#proteins"));
        List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
        assertThat(proteins.size()).isEqualTo(2);
        assertIngredient(proteins, 0, "GRBF", "Ground Beef");
        assertIngredient(proteins, 1, "CARN", "Carnitas");

        WebElement cheeseGroup = browser.findElement(By.cssSelector("div.ingredient-group#cheeses"));
        List<WebElement> cheeses = cheeseGroup.findElements(By.tagName("div"));
        assertThat(cheeses.size()).isEqualTo(2);
        assertIngredient(cheeses, 0, "CHED", "Cheddar");
        assertIngredient(cheeses, 1, "JACK", "Monterrey Jack");

        WebElement veggieGroup = browser.findElement(By.cssSelector("div.ingredient-group#veggies"));
        List<WebElement> veggies = veggieGroup.findElements(By.tagName("div"));
        assertThat(veggies.size()).isEqualTo(2);
        assertIngredient(veggies, 0, "TMTO", "Diced Tomatoes");
        assertIngredient(veggies, 1, "LETC", "Lettuce");

        WebElement sauceGroup = browser.findElement(By.cssSelector("div.ingredient-group#sauces"));
        List<WebElement> sauces = sauceGroup.findElements(By.tagName("div"));
        assertThat(sauces.size()).isEqualTo(2);
        assertIngredient(sauces, 0, "SLSA", "Salsa");
        assertIngredient(sauces, 1, "SRCR", "Sour Cream");
    }

    private String designPageUrl() {
        return homePageUrl() + "design";
    }

    private String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    private void assertIngredient(List<WebElement> ingredients, int index, String id, String name) {
        WebElement ingredient = ingredients.get(index);
        assertThat(ingredient.findElement(By.tagName("input")).getAttribute("value"))
                .isEqualTo(id);
        assertThat(ingredient.findElement(By.tagName("span")).getText())
                .isEqualTo(name);
    }

}
