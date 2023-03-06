package tacos.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IngredientByIdConverterTest {

    private IngredientByIdConverter converter;

    @BeforeEach
    void setup() {
        IngredientRepository ingredientRepo = mock(IngredientRepository.class);
        when(ingredientRepo.findOne("AAAA")).thenReturn(Optional.of(new Ingredient("AAAA", "TEST INGREDIENT", Ingredient.Type.CHEESE)));
        when(ingredientRepo.findOne("ZZZZ")).thenReturn(Optional.empty());

        this.converter = new IngredientByIdConverter(ingredientRepo);
    }

    @Test
    public void shouldReturnValue_WhenPresent() {
        assertThat(converter.convert("AAAA"))
                .isEqualTo(new Ingredient("AAAA", "TEST INGREDIENT", Ingredient.Type.CHEESE));
    }

    @Test
    public void shouldReturnNull_WhenMissing() {
        assertThat(converter.convert("ZZZZ")).isNull();
    }
}