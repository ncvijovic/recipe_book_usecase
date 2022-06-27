package com.valcon.recipebook.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.valcon.recipebook.config.AbstractFuncTest;
import com.valcon.recipebook.model.dao.Ingredient;
import com.valcon.recipebook.model.dao.IngredientRecipe;
import com.valcon.recipebook.model.dao.IngredientRecipeId;
import com.valcon.recipebook.model.dao.Recipe;

class RecipeRepositoryFuncTest extends AbstractFuncTest {

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
    void saveRecipeTest() {
        Recipe recipe = createRecipe();
        Recipe saved = repository.save(recipe);
        saved.setIngredientRecipes(createIngredients(saved));
        repository.save(saved);

        List<Recipe> recipes = repository.findAll();
        assertThat(recipes.size()).isEqualTo(1);
        Recipe result = recipes.get(0);
        assertThat(result.getName()).isEqualTo("crepes");
        assertThat(result.getServings()).isEqualTo(4);
        assertThat(result.getVegetarian()).isFalse();
        assertThat(result.getCookingInstructions()).isEqualTo("mix it all");
        assertThat(result.getIngredientRecipes().size()).isEqualTo(3);
        assertThat(result.getIngredientRecipes().stream().map(ir -> ir.getIngredient().getName()).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder("salt", "milk", "eggs");
    }

    @Test
    void deleteRecipeTest() {
        Recipe recipe = createRecipe();
        Recipe saved = repository.save(recipe);
        saved.setIngredientRecipes(createIngredients(saved));
        repository.save(saved);

        repository.deleteById(saved.getId());

        assertThat(repository.findAll().size()).isZero();
    }

    @Test
    void findRecipeTest() {
        Recipe recipe = createRecipe();
        Recipe saved = repository.save(recipe);
        saved.setIngredientRecipes(createIngredients(saved));
        repository.save(saved);

        Optional<Recipe> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
    }

    private Recipe createRecipe() {
        return Recipe.builder()
                     .name("crepes")
                     .vegetarian(false)
                     .servings(4)
                     .cookingInstructions("mix it all")
                     .creationDate(LocalDateTime.now())
                     .build();
    }

    private Set<IngredientRecipe> createIngredients(Recipe recipe) {
        return Stream.of("salt", "milk", "eggs")
                     .map(s -> ingredientRepository.save(Ingredient.builder().name(s).build()))
                     .map(i -> IngredientRecipe.builder()
                                               .id(new IngredientRecipeId(recipe.getId(), i.getId()))
                                               .ingredient(i)
                                               .recipe(recipe)
                                               .amount(1)
                                               .unit("pinch")
                                               .build())
                     .collect(Collectors.toSet());
    }
}
