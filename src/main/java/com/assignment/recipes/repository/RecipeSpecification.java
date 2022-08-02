package com.assignment.recipes.repository;

import com.assignment.recipes.model.Recipe;
import com.assignment.recipes.model.Recipe.Fields;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class RecipeSpecification {

    public Specification<Recipe> isVegan(Boolean vegan) {
        return (recipe, query, builder) ->
            builder.equal(recipe.get(Fields.vegan), vegan);
    }

    public Specification<Recipe> servingNumber(Integer serving) {
        return (recipe, query, builder) ->
            builder.equal(recipe.get(Fields.servings), serving);
    }

    public Specification<Recipe> withIngredients(List<String> with) {
        return (recipe, query, builder) ->
            builder.and(recipe.join(Fields.ingredients).in(with));
    }

    public Specification<Recipe> withoutIngredients(List<String> without) {
        return (recipe, query, builder) -> {
            query.distinct(true);
            final Subquery<String> include = query.subquery(String.class);
            final Root<Recipe> subRoot = include.from(Recipe.class);
            include.select(subRoot.get(Fields.id));
            include.where(builder.in(subRoot.join(Fields.ingredients)).value(without));
            return builder.and(recipe.get(Fields.id).in(include).not());
        };
    }

    public Specification<Recipe> instructions(String instructions) {
        return (recipe, query, builder) -> {
            query.distinct(true);
            return builder.like(recipe.get(Fields.instructions), "%"+instructions+"%");
        };
    }
}
