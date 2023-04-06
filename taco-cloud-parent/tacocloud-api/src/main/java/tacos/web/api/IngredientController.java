package tacos.web.api;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Ingredient;
import tacos.data.IngredientRepository;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/ingredients", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class IngredientController {
    private IngredientRepository repo;

    @GetMapping
    public ResponseEntity<Iterable<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(repo.findAll());
    }

    @PostMapping
    public ResponseEntity<Ingredient> saveIngredient(@RequestBody Ingredient ingredient) {
        return ResponseEntity.created(URI.create("/api/ingredients")).body(repo.save(ingredient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable String id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
