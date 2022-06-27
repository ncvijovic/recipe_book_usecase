package com.valcon.recipebook.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valcon.recipebook.model.dao.Ingredient;
import com.valcon.recipebook.model.dao.IngredientRecipe;
import com.valcon.recipebook.model.dao.IngredientRecipeId;
import com.valcon.recipebook.model.dao.Recipe;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.model.dto.RecipeDto;
import com.valcon.recipebook.model.mappers.IngredientMapper;
import com.valcon.recipebook.model.mappers.RecipeMapper;
import com.valcon.recipebook.repositories.RecipeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final IngredientService ingredientService;
    private final RecipeMapper mapper;
    private final IngredientMapper ingredientMapper;

    @Override
    public RecipeDto save(RecipeDto recipeDto) {
        log.info("Saving recipe named {}", recipeDto.name());
        Recipe recipe = mapper.map(recipeDto);
        Recipe saved = repository.save(recipe);

        Set<IngredientRecipe> ingredients = getIngredientRecipes(recipeDto, saved);
        saved.setIngredientRecipes(ingredients);

        return mapResponse(repository.save(saved));
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting recipe with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public RecipeDto find(Long id) {
        return repository.findById(id).map(this::mapResponse).orElseThrow();
    }

    @Override
    public List<RecipeDto> findAll() {
        return repository.findAll().stream().map(this::mapResponse).toList();
    }

    @Override
    public RecipeDto update(Long id, RecipeDto recipeDto) {
        log.info("Updating recipe with id {}", id);
        Recipe recipe = repository.findById(id).orElseThrow();
        mapper.map(recipeDto, recipe);
        Set<IngredientRecipe> ingredients = getIngredientRecipes(recipeDto, recipe);
        recipe.addIngredients(ingredients);
        return mapResponse(repository.save(recipe));
    }

    private Set<IngredientRecipe> getIngredientRecipes(RecipeDto recipeDto, Recipe recipe) {
        return recipeDto.ingredients().stream().map(dto -> {
            Ingredient ingredient = dto.id() == null ?
                    ingredientMapper.map(ingredientService.save(dto)) : ingredientMapper.map(ingredientService.find(dto.id()));
            return IngredientRecipe.builder()
                                   .id(new IngredientRecipeId(recipe.getId(), ingredient.getId()))
                                   .recipe(recipe).ingredient(ingredient)
                                   .unit(dto.unit())
                                   .amount(dto.amount())
                                   .build();
        }).collect(Collectors.toSet());
    }

    private RecipeDto mapResponse(Recipe recipe) {
        Set<IngredientDto> ingredients = mapIngredientsResponse(recipe.getIngredientRecipes());
        return mapper.map(recipe, ingredients);
    }

    private Set<IngredientDto> mapIngredientsResponse(Set<IngredientRecipe> ingredients) {
        return ingredients.stream()
                          .map(ir -> ingredientMapper.map(ir.getIngredient(), ir.getAmount(), ir.getUnit()))
                          .collect(Collectors.toSet());
    }
}
