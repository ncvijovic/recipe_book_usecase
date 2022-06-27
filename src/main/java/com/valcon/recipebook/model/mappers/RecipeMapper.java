package com.valcon.recipebook.model.mappers;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.valcon.recipebook.model.dao.Recipe;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.model.dto.RecipeDto;

@Component
public class RecipeMapper {

    public Recipe map(RecipeDto dto) {
        return Recipe.builder()
                     .id(dto.id())
                     .name(dto.name())
                     .vegetarian(dto.vegetarian())
                     .servings(dto.servings())
                     .cookingInstructions(dto.cookingInstructions())
                     .creationDate(LocalDateTime.now())
                     .build();
    }

    public RecipeDto map(Recipe recipe) {
        return getRecipeDtoBuilder(recipe)
                        .build();
    }

    public RecipeDto map(Recipe recipe, Set<IngredientDto> ingredients) {
        return getRecipeDtoBuilder(recipe)
                        .ingredients(ingredients)
                        .build();
    }

    public void map(RecipeDto dto, Recipe recipe) {
        recipe.setName(dto.name());
        recipe.setCookingInstructions(dto.cookingInstructions());
        recipe.setServings(dto.servings());
        recipe.setVegetarian(dto.vegetarian());
    }

    private RecipeDto.RecipeDtoBuilder getRecipeDtoBuilder(Recipe recipe) {
        return RecipeDto.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .vegetarian(recipe.getVegetarian())
                        .creationDate(recipe.getCreationDate())
                        .servings(recipe.getServings())
                        .cookingInstructions(recipe.getCookingInstructions());
    }
}
