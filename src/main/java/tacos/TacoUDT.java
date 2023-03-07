package tacos;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.List;

@UserDefinedType("taco")
public record TacoUDT(String name, List<IngredientUDT> ingredients) {
}
