package tacos;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;

public class RestIngredientService implements IngredientService {

    private RestTemplate restTemplate;

    public RestIngredientService(String accessToken) {
        this.restTemplate = new RestTemplate();
        if (accessToken != null) {
            this.restTemplate
                    .getInterceptors()
                    .add((request, bytes, execution) -> {
                        request.getHeaders().add("Authorization", "Bearer " + accessToken);
                        return execution.execute(request, bytes);
                    });
        }
    }


    @Override
    public Iterable<Ingredient> findAll() {
        return Arrays.asList(
                Objects.requireNonNull(
                        restTemplate.getForObject("http://localhost:8080/api/ingredients", Ingredient[].class)
                )
        );
    }

    @Override
    public Ingredient addIngredient(Ingredient ingredient) {
        return restTemplate.getForObject("http://localhost:8080/api/ingredients", Ingredient.class, ingredient);
    }
}
