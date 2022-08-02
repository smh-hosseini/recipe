package com.assignment.recipes.resource.mapper;

import static org.junit.Assert.assertEquals;

import com.assignment.recipes.model.Recipe;
import com.assignment.recipes.resource.mapping.RecipeMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RecipeMapperTest {

    @Test
    public void givenRecipeEntityToApiModel() {

        Recipe entity = new Recipe();
        entity.setId(1L);
        entity.setName("Recipe1");
        entity.setServings(2);
        entity.setVegan(true);
        entity.setIngredients(List.of("ingredient1"));
        entity.setInstructions("Instructions");

        com.assignment.recipes.resources.api.model.Recipe recipe = RecipeMapper.INSTANCE.mapToApiModel(entity);

        assertEquals(recipe.getId(), entity.getId());
        assertEquals(recipe.getName(), entity.getName());
        assertEquals(recipe.getVegan(), entity.getVegan());
        assertEquals(recipe.getServings(), entity.getServings());
        assertEquals(recipe.getInstructions(), entity.getInstructions());
        assertEquals(recipe.getIngredients(), entity.getIngredients());
    }

    @Test
    public void givenRecipeApiModelToEntity() {

        com.assignment.recipes.resources.api.model.Recipe recipe = new com.assignment.recipes.resources.api.model.Recipe();
        recipe.setId(1L);
        recipe.setName("Recipe1");
        recipe.setServings(2);
        recipe.setVegan(true);
        recipe.setIngredients(List.of("ingredient1"));
        recipe.setInstructions("Instructions");

        Recipe entityModel = RecipeMapper.INSTANCE.mapToEntityModel(recipe);

        assertEquals(entityModel.getId(), recipe.getId());
        assertEquals(entityModel.getName(), recipe.getName());
        assertEquals(entityModel.getVegan(), recipe.getVegan());
        assertEquals(entityModel.getServings(), recipe.getServings());
        assertEquals(entityModel.getInstructions(), recipe.getInstructions());
        assertEquals(entityModel.getIngredients(), recipe.getIngredients());
    }

}
