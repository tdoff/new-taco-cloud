package tacos;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("ingredient")
public record IngredientUDT(String name, Ingredient.Type type) {
}
