package com.assignment.recipes.service;

import com.assignment.recipes.model.Recipe;
import com.assignment.recipes.repository.RecipeRepository;
import com.assignment.recipes.repository.RecipeSpecification;
import com.assignment.recipes.repository.SpecificationBuilder;
import com.assignment.recipes.service.exception.RecipeNotFoundException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeSpecification recipeSpecification;


    @Override
    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe Not found for "
            + "this id :" + id));
    }

    @Override
    public List<Recipe> getRecipes(Boolean vegetarian, Integer servings, Set<String> includes, Set<String> excludes,
        String instruction) {

        SpecificationBuilder<Recipe> specificationBuilder = new SpecificationBuilder<>();
        specificationBuilder.andOnCondition(vegetarian != null, () -> recipeSpecification.isVegan(vegetarian));
        specificationBuilder.andOnCondition(servings != null, () -> recipeSpecification.servingNumber(servings));
        specificationBuilder.andOnCondition(includes != null, () -> recipeSpecification.withIngredients(includes.stream().toList()));
        specificationBuilder.andOnCondition(excludes != null,
            () -> recipeSpecification.withoutIngredients(excludes.stream().toList()));
        specificationBuilder.andOnCondition(instruction != null,
            () -> recipeSpecification.instructions(instruction));
        return recipeRepository.findAll(specificationBuilder.build());
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        recipeRepository.deleteById(recipeId);
    }
}
