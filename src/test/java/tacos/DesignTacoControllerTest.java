package tacos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tacos.Ingredient.Type;
import tacos.data.IngredientRepository;
import tacos.web.DesignTacoController;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DesignTacoController.class)
class DesignTacoControllerTest {

    private Map<String, List<Ingredient>> ingredientMap = new HashMap<>();
    private Taco design;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IngredientRepository ingredientRepo;

    @BeforeEach
    void setup() {
        ingredientMap.put(Type.WRAP.name(),
                Arrays.asList(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP), new Ingredient("COTO", "Corn Tortilla", Type.WRAP)));
        ingredientMap.put(Type.PROTEIN.name(),
                Arrays.asList(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN), new Ingredient("CARN", "Carnitas", Type.PROTEIN)));
        ingredientMap.put(Type.VEGGIES.name(),
                Arrays.asList(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES), new Ingredient("LETC", "Lettuce", Type.VEGGIES)));
        ingredientMap.put(Type.CHEESE.name(),
                Arrays.asList(new Ingredient("CHED", "Cheddar", Type.CHEESE), new Ingredient("JACK", "Monterrey Jack", Type.CHEESE)));
        ingredientMap.put(Type.SAUCE.name(),
                Arrays.asList(new Ingredient("SLSA", "Salsa", Type.SAUCE), new Ingredient("SRCR", "Sour Cream", Type.SAUCE)));
        when(ingredientRepo.findAll())
                .thenReturn(ingredientMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList()));

        when(ingredientRepo.findById("FLTO"))
                .thenReturn(Optional.of(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP)));
        when(ingredientRepo.findById("GRBF"))
                .thenReturn(Optional.of(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN)));
        when(ingredientRepo.findById("CHED"))
                .thenReturn(Optional.of(new Ingredient("CHED", "Cheddar", Type.CHEESE)));

        design = new Taco();
        design.setName("Test Taco");
        design.setIngredients(
                Arrays.asList(new IngredientRef("FLTO"), new IngredientRef("GRBF"), new IngredientRef("CHED")));
    }

    @Test
    void showDesignForm() throws Exception {
        mockMvc.perform(get("/design"))
                .andExpect(status().isOk())
                .andExpect(view().name("design"))
                .andExpect(model().attribute("wrap", ingredientMap.get(Type.WRAP.name())))
                .andExpect(model().attribute("protein", ingredientMap.get(Type.PROTEIN.name())))
                .andExpect(model().attribute("veggies", ingredientMap.get(Type.VEGGIES.name())))
                .andExpect(model().attribute("cheese", ingredientMap.get(Type.CHEESE.name())))
                .andExpect(model().attribute("sauce", ingredientMap.get(Type.SAUCE.name())));
    }

    @Test
    void processTaco() throws Exception {
        mockMvc.perform(post("/design")
                        .content("name=Taco+Test&ingredients=FLTO,GRBF,CHED")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().stringValues("Location", "/orders/current"));
    }
}