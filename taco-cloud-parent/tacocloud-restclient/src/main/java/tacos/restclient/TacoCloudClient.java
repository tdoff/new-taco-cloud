package tacos.restclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tacos.Ingredient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TacoCloudClient {

    private String prefixUrl = "https://localhost:8443";
    private final RestTemplate restTemplate;

//    public Ingredient getIngredientById(String ingredientId) {
//        return restTemplate.getForObject(prefixUrl + "/ingredients/{id}", Ingredient.class, ingredientId);
//    }

//    public Ingredient getIngredientById(String ingredientId) {
//        return restTemplate.getForObject(prefixUrl + "/ingredients/{id}", Ingredient.class, Map.of("id", ingredientId));
//    }

//    public Ingredient getIngredientById(String ingredientId) {
//        URI url = UriComponentsBuilder.fromHttpUrl(prefixUrl + "/ingredients/{id}").build(Map.of("id", ingredientId));
//        return restTemplate.getForObject(url, Ingredient.class);
//    }

    public Ingredient getIngredientById(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity = restTemplate.getForEntity(prefixUrl + "/ingredients/{id}", Ingredient.class, ingredientId);
        log.info("Fetched time: {}", responseEntity.getHeaders().getDate());
        return responseEntity.getBody();
    }

    public List<Ingredient> getAllIngredients() {
        return restTemplate.exchange("http://localhost:8080/ingredients",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Ingredient>>() {})
                .getBody();
    }

    public void updateIngredient(Ingredient ingredient) {
        restTemplate.put(prefixUrl + "/ingredients/{id}", ingredient, ingredient.getId());
    }

    public void deleteIngredient(Ingredient ingredient) {
        restTemplate.delete(prefixUrl + "/ingredients/{id}", ingredient.getId());
    }

//    public Ingredient createIngredient(Ingredient ingredient) {
//        return restTemplate.postForObject(prefixUrl + "/ingredients", ingredient, Ingredient.class);
//    }

//    public URI createIngredient(Ingredient ingredient) {
//        return restTemplate.postForLocation(prefixUrl + "/ingredients", ingredient);
//    }

    public Ingredient createIngredient(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity = restTemplate.postForEntity(prefixUrl + "/ingredients", ingredient, Ingredient.class);
        log.info("New resource created at {}", responseEntity.getHeaders().getLocation());
        return responseEntity.getBody();
    }
}

