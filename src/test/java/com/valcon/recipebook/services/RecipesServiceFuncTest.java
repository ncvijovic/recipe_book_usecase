package com.valcon.recipebook.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.valcon.recipebook.config.AbstractFuncTest;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.model.dto.RecipeDto;
import com.valcon.recipebook.repositories.IngredientRepository;
import com.valcon.recipebook.repositories.RecipeRepository;

class RecipesServiceFuncTest extends AbstractFuncTest {

    @Autowired
    private RecipeService service;

    @Autowired
    private RecipeRepository repository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
        ingredientRepository.deleteAll();
    }

    @Test
    void createRecipeTest() {
        RecipeDto recipe = createRecipe();
        RecipeDto result = service.save(recipe);

        assertThat(result.name()).isEqualTo(recipe.name());
        assertThat(result.cookingInstructions()).isEqualTo(recipe.cookingInstructions());
        assertThat(result.servings()).isEqualTo(recipe.servings());
        assertThat(result.vegetarian()).isEqualTo(recipe.vegetarian());
        assertThat(result.ingredients().size()).isEqualTo(recipe.ingredients().size());
    }

    @Test
    void updateRecipeTest() {
        RecipeDto recipe = createRecipe();
        RecipeDto saved = service.save(recipe);
        RecipeDto update = RecipeDto.builder().name("crepes").cookingInstructions("mix it up").servings(1).vegetarian(false)
                                         .ingredients(saved.ingredients()).build();

        RecipeDto modified = service.update(saved.id(), update);

        assertThat(modified.servings()).isEqualTo(1);
        assertThat(modified.ingredients().stream().map(IngredientDto::id).toList())
                .containsExactlyElementsOf(saved.ingredients().stream().map(IngredientDto::id).toList());
    }

    @Test
    void deleteRecipeTest() {
        RecipeDto recipe = createRecipe();
        RecipeDto saved = service.save(recipe);

        service.delete(saved.id());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> service.find(saved.id()));
    }

    @Test
    void findAllTest() {
        RecipeDto crepes = createRecipe();
        service.save(crepes);
        RecipeDto pancakes = createRecipe("pancakes");
        service.save(pancakes);

        List<RecipeDto> recipes = service.findAll();

        assertThat(recipes.size()).isEqualTo(2);
        assertThat(recipes.stream().map(RecipeDto::name).toList()).containsExactlyInAnyOrder("crepes", "pancakes");
    }

    private RecipeDto createRecipe() {
        return createRecipe("crepes", "flour", "egg", "milk");
    }
    private RecipeDto createRecipe(String name, String... ingredientNames) {
        return RecipeDto.builder()
                        .name(name)
                        .cookingInstructions("mix it up")
                        .servings(4)
                        .vegetarian(false)
                        .ingredients(createIngredients(ingredientNames))
                        .build();
    }

    private Set<IngredientDto> createIngredients(String... names) {
        return Stream.of(names)
                     .map(s -> IngredientDto.builder().name(s).amount(1).unit("handful").build())
                     .collect(Collectors.toSet());

    }
}
