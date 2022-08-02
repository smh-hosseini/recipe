package com.assignment.recipes.resource;

import static org.springframework.http.ResponseEntity.created;

import com.assignment.recipes.resource.mapping.RecipeMapper;
import com.assignment.recipes.resources.api.RecipesApi;
import com.assignment.recipes.resources.api.model.Recipe;
import com.assignment.recipes.service.RecipeService;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class RecipeResource implements RecipesApi {

    private final RecipeService recipeService;
    private final RecipeMapper mapper;

    @Override
    public CompletableFuture<ResponseEntity<Void>> createRecipes(Recipe recipe) {
        return CompletableFuture.supplyAsync(() -> {
            final UriComponentsBuilder uriComponentsBuilder = createUri();
            final var createRecipe =  recipeService.createRecipe(mapper.mapToEntityModel(recipe));
            final var uriComponents = uriComponentsBuilder
                .buildAndExpand(createRecipe.getId());
            return created(uriComponents.toUri()).build();
        });
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> deleteRecipe(Long recipeId) {
        return CompletableFuture.supplyAsync(() -> {
            recipeService.deleteRecipe(recipeId);
            return ResponseEntity.ok().build();
        });
    }

    @Override
    public CompletableFuture<ResponseEntity<Recipe>> getRecipe(Long recipeId) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(mapper.mapToApiModel(recipeService.getRecipe(recipeId))));
    }

    @Override
    public CompletableFuture<ResponseEntity<List<Recipe>>> getRecipes(Boolean vegetarian, Integer servings,
        Set<String> ingredientsInclude, Set<String> ingredientsExclude, String instruction) {
        return CompletableFuture.supplyAsync(() -> {
            final List<Recipe> recipes = recipeService.getRecipes(vegetarian, servings,
                ingredientsInclude,
                ingredientsExclude, instruction).stream().map(recipe -> mapper.mapToApiModel(recipe)).toList();
            return ResponseEntity.ok(recipes);
        });
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> updateRecipes(Recipe recipe) {
        return CompletableFuture.supplyAsync(() -> {
            recipeService.updateRecipe(mapper.mapToEntityModel(recipe));
            return ResponseEntity.ok().build();
        });
    }

    private UriComponentsBuilder createUri() {
        return UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .port("8080")
            .path("/v1/recipes/{id"
            + "}");
    }
}
