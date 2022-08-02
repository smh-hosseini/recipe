package com.assignment.recipes.resource;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assignment.recipes.BaseApiTest;
import com.assignment.recipes.repository.RecipeRepository;
import com.assignment.recipes.resources.api.model.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;


public class RecipeApiTest extends BaseApiTest {

    private static final String V1_RECIPE = "/v1/recipes";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RecipeRepository recipeRepository;

    @BeforeEach
    void beforeEach() {
        recipeRepository.deleteAll();
    }

    @Test
    void createRecipe() throws Exception {
        Recipe recipe =
            new Recipe().servings(2).name("Recipe 3").vegan(true).addIngredientsItem("butter").instructions(
                "Using anything but oven.");

        final MockHttpServletResponse response = mockMvc.perform(asyncDispatch(mockMvc.perform(
                post(V1_RECIPE).contentType(APPLICATION_JSON).content(this.mapper.writeValueAsString(recipe)))
            .andReturn())).andExpect(status().isCreated()).andReturn().getResponse();
        final Long recipeId = getCreatedRecipeId(response);
        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE + "/{recipeId}", recipeId))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(recipe.getName())))
            .andExpect(jsonPath("$.vegan", is(recipe.getVegan())))
            .andExpect(jsonPath("$.instructions", is(recipe.getInstructions())));
    }

    @Test
    void createRecipeException() throws Exception {
        Recipe recipe =
            new Recipe().servings(2).name("Recipe 3").vegan(true).addIngredientsItem("butter").instructions(
                "Using anything but oven.");

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                post(V1_RECIPE).contentType(APPLICATION_JSON).content(this.mapper.writeValueAsString(recipe)))
            .andReturn())).andExpect(status().isCreated());

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                post(V1_RECIPE).contentType(APPLICATION_JSON).content(this.mapper.writeValueAsString(recipe)))
            .andReturn())).andExpect(status().isBadRequest());
    }

    @Test
    void updateRecipe() throws Exception {
        Recipe recipe =
            new Recipe().servings(2).name("Recipe 1").vegan(true).addIngredientsItem("butter").instructions(
                "Using anything but oven.");

        final MockHttpServletResponse response = mockMvc.perform(asyncDispatch(mockMvc.perform(
                post(V1_RECIPE).contentType(APPLICATION_JSON).content(this.mapper.writeValueAsString(recipe)))
            .andReturn())).andExpect(status().isCreated()).andReturn().getResponse();

        final Long recipeId = getCreatedRecipeId(response);

        recipe.setId(recipeId);
        recipe.addIngredientsItem("flour");

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                put(V1_RECIPE).contentType(APPLICATION_JSON).content(this.mapper.writeValueAsString(recipe)))
            .andReturn()))
            .andExpect(status().isOk());

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE + "/{recipeId}", recipeId))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(recipe.getName())))
            .andExpect(jsonPath("$.vegan", is(recipe.getVegan())))
            .andExpect(jsonPath("$.ingredients", iterableWithSize(2)))
            .andExpect(jsonPath("$.ingredients", is(List.of("butter","flour"))));

    }

    @Test
    void deleteRecipe() throws Exception {
        Recipe recipe =
            new Recipe().servings(2).name("Recipe 1").vegan(true).addIngredientsItem("butter").instructions(
                "Using anything but oven.");

        final MockHttpServletResponse response = mockMvc.perform(asyncDispatch(mockMvc.perform(
                post(V1_RECIPE).contentType(APPLICATION_JSON).content(this.mapper.writeValueAsString(recipe)))
            .andReturn())).andExpect(status().isCreated()).andReturn().getResponse();

        final Long recipeId = getCreatedRecipeId(response);

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    delete(V1_RECIPE + "/{recipeId}", recipeId))
                .andReturn()))
            .andExpect(status().isOk());

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE + "/{recipeId}", recipeId))
                .andReturn()))
            .andExpect(status().isNotFound());
    }

    @Test
    void filterRecipesVegan() throws Exception {
        createRecipe("Omlet", 2, true, List.of("egg", "butter", "tommato"), "Just put all of that in the pan.");
        createRecipe("Rice", 2, true, List.of("rice", "butter"), "Just put all of that in the oven.");
        createRecipe("Steak", 4, false, List.of("meet", "vegetables"), "Just put all of that in the oven.");

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE + "?vegan=false"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].name", is("Steak")))
            .andExpect(jsonPath("$.[0].ingredients", iterableWithSize(2)))
            .andExpect(jsonPath("$.[0].ingredients", is(List.of("meet", "vegetables"))));

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE+"?vegan=true"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$.[0].name", is("Omlet")))
            .andExpect(jsonPath("$.[1].name", is("Rice")));
    }

    @Test
    void filterRecipesServings() throws Exception {
        createRecipe("Omlet", 2, true, List.of("egg", "butter", "tomato"), "Just put all of that in the pan.");
        createRecipe("Rice", 2, true, List.of("rice", "butter"), "Just put all of that in the oven.");
        createRecipe("Steak", 4, false, List.of("meet", "vegetables"), "Just put all of that in the oven.");

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE + "?servings=2"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$.[0].name", is("Omlet")))
            .andExpect(jsonPath("$.[1].name", is("Rice")));

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE+"?servings=4"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].name", is("Steak")));
    }


    @Test
    void filterRecipesInstructions() throws Exception {
        createRecipe("Omlet", 2, true, List.of("egg", "butter", "tomato"), "Just put all of that in the pan.");
        createRecipe("Rice", 2, true, List.of("rice", "butter"), "Just put all of that in the oven.");
        createRecipe("Steak", 4, false, List.of("meet", "vegetables"), "Just put all of that in the oven.");

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE + "?instruction=pan"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].name", is("Omlet")));

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE+"?instruction=oven"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$.[0].name", is("Rice")))
            .andExpect(jsonPath("$.[1].name", is("Steak")));
    }

    @Test
    void filterRecipesIngredient() throws Exception {
        createRecipe("Omlet", 2, true, List.of("egg", "butter", "tomato"), "Just put all of that in the pan.");
        createRecipe("Rice", 2, true, List.of("rice", "butter"), "Just put all of that in the oven.");
        createRecipe("Steak", 4, false, List.of("meet", "vegetables"), "Just put all of that in the oven.");

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE + "?ingredients-include=butter"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$.[0].name", is("Omlet")))
            .andExpect(jsonPath("$.[1].name", is("Rice")));

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE+"?ingredients-exclude=butter"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].name", is("Steak")));

        createRecipe("Golash", 3, false, List.of("meet", "vegetables", "tomato"), "Just put all of that in the oven.");

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE+"?ingredients-exclude=butter&servings=3"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].name", is("Golash")));

        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE+"?ingredients-exclude=butter&ingredients-include=tomato"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].name", is("Golash")));


        mockMvc.perform(asyncDispatch(mockMvc.perform(
                    get(V1_RECIPE+"?instruction=pan&ingredients-include=tomato"))
                .andReturn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].name", is("Omlet")));
    }


    private Long getCreatedRecipeId(MockHttpServletResponse response) {
        final String location = response.getHeaderValue("Location").toString();
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }


    private void createRecipe(String name, Integer servings, Boolean vegan, List<String> ingredients,
        String instructions) throws Exception {
        Recipe recipe =
            new Recipe().name(name).servings(servings).vegan(vegan).ingredients(ingredients).instructions(instructions);
        mockMvc.perform(asyncDispatch(mockMvc.perform(
                post(V1_RECIPE).contentType(APPLICATION_JSON).content(this.mapper.writeValueAsString(recipe)))
            .andReturn())).andExpect(status().isCreated());
    }
}
