package com.assignment.recipes.service;

import com.assignment.recipes.model.Recipe;
import java.util.List;
import java.util.Set;

public interface RecipeService {

    Recipe getRecipe(Long id);

    List<Recipe> getRecipes(Boolean vegetarian, Integer servings, Set<String> include, Set<String> exclude,
        String instruction);

    Recipe createRecipe(Recipe recipe);

    void updateRecipe(Recipe recipe);

    void deleteRecipe(Long recipeId);

}
