package com.valcon.recipebook.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valcon.recipebook.model.dao.Ingredient;
import com.valcon.recipebook.model.dao.IngredientRecipe;
import com.valcon.recipebook.model.dao.Recipe;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.model.dto.RecipeDto;
import com.valcon.recipebook.model.mappers.IngredientMapper;
import com.valcon.recipebook.model.mappers.RecipeMapper;
import com.valcon.recipebook.repositories.RecipeRepository;

@ExtendWith(MockitoExtension.class)
class RecipeServiceUnitTest {

    @InjectMocks
    private RecipeServiceImpl recipeService;
    @Mock
    private RecipeRepository repository;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private RecipeMapper mapper;
    @Mock
    private IngredientMapper ingredientMapper;

    @Test
    void createRecipeTest() {
        Recipe recipe = createRecipe();
        RecipeDto dto = createRecipeDto();

        when(mapper.map(dto)).thenReturn(recipe, recipe);
        when(mapper.map(any(Recipe.class), anySet())).thenReturn(dto);
        when(repository.save(recipe)).thenReturn(recipe, recipe);
        when(ingredientService.save(any(IngredientDto.class))).thenReturn(dto.ingredients().iterator().next());
        when(ingredientMapper.map(any(IngredientDto.class))).thenReturn(recipe.getIngredientRecipes().iterator().next().getIngredient());

        RecipeDto result = recipeService.save(dto);

        assertEquals(result, recipe);
        assertThat(result.name()).isEqualTo(recipe.getName());
    }

    @Test
    void updateRecipeTest() {
        Recipe recipe = createRecipe();
        RecipeDto dto = createRecipeDto();
        RecipeDto updatedDto = createRecipeDto("soup");

        when(repository.findById(1L)).thenReturn(Optional.of(recipe));
        doNothing().when(mapper).map(dto, recipe);
        when(ingredientService.save(any(IngredientDto.class))).thenReturn(dto.ingredients().iterator().next());
        when(ingredientMapper.map(any(IngredientDto.class))).thenReturn(recipe.getIngredientRecipes().iterator().next().getIngredient());
        when(repository.save(recipe)).thenReturn(recipe);
        when(mapper.map(any(Recipe.class), anySet())).thenReturn(updatedDto);

        RecipeDto result = recipeService.update(1L, dto);

        assertEquals(result, recipe);
        assertThat(result.name()).isEqualTo(updatedDto.name());
    }

    @Test
    void deleteRecipeTest() {
        doNothing().when(repository).deleteById(1L);

        recipeService.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void findRecipeTest() {
        Recipe recipe = createRecipe();
        RecipeDto dto = createRecipeDto();

        when(repository.findById(1L)).thenReturn(Optional.of(recipe));
        when(mapper.map(any(Recipe.class), anySet())).thenReturn(dto);

        RecipeDto result = recipeService.find(1L);

        assertEquals(result, recipe);
    }

    private void assertEquals(RecipeDto result, Recipe recipe) {
        assertThat(result.id()).isEqualTo(recipe.getId());
        assertThat(result.servings()).isEqualTo(recipe.getServings());
        assertThat(result.vegetarian()).isEqualTo(recipe.getVegetarian());
        assertThat(result.cookingInstructions()).isEqualTo(recipe.getCookingInstructions());
        assertThat(result.creationDate()).isEqualTo(recipe.getCreationDate());
        Set<IngredientDto> resultIngredients = result.ingredients();
        Set<IngredientRecipe> ingredients = recipe.getIngredientRecipes();
        assertThat(resultIngredients.size()).isEqualTo(ingredients.size());
        IngredientDto resultIngredient = resultIngredients.iterator().next();
        IngredientRecipe ingredientRecipe = ingredients.iterator().next();
        Ingredient ingredient = ingredientRecipe.getIngredient();
        assertThat(resultIngredient.name()).isEqualTo(ingredient.getName());
        assertThat(resultIngredient.unit()).isEqualTo(ingredientRecipe.getUnit());
        assertThat(resultIngredient.amount()).isEqualTo(ingredientRecipe.getAmount());
    }

    private Recipe createRecipe() {
        Recipe recipe = Recipe.builder().id(1L).servings(1).cookingInstructions("instructions").vegetarian(false).name("dish")
                              .creationDate(LocalDateTime.of(2020, 1, 1, 0, 0)).build();
        Set<IngredientRecipe> ingredients = createIngredients(recipe);
        recipe.setIngredientRecipes(ingredients);
        return recipe;
    }

    private Set<IngredientRecipe> createIngredients(Recipe recipe) {
        Ingredient salt = Ingredient.builder().name("salt").id(1L).build();
        IngredientRecipe ingredient = IngredientRecipe.builder().unit("pinch").amount(1).ingredient(salt).recipe(recipe).build();
        return Sets.set(ingredient);
    }

    private RecipeDto createRecipeDto() {
        return createRecipeDto("dish");
    }
    private RecipeDto createRecipeDto(String name) {
        return RecipeDto.builder().id(1L).name(name).servings(1).vegetarian(false).cookingInstructions("instructions")
                        .creationDate(LocalDateTime.of(2020, 1, 1, 0, 0)).ingredients(createIngredientsDto()).build();
    }

    private Set<IngredientDto> createIngredientsDto() {
        return Sets.set(IngredientDto.builder().name("salt").unit("pinch").amount(1).build());
    }
}
