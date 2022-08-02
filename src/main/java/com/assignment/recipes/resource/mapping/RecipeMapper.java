package com.assignment.recipes.resource.mapping;

import com.assignment.recipes.resources.api.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    Recipe mapToApiModel(com.assignment.recipes.model.Recipe recipe);

    com.assignment.recipes.model.Recipe mapToEntityModel(Recipe recipe);
}
