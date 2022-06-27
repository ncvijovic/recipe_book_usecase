package com.valcon.recipebook.services;

import java.util.List;

import com.valcon.recipebook.model.dto.RecipeDto;

public interface RecipeService {

    RecipeDto save(RecipeDto recipeDto);

    void delete(Long id);

    RecipeDto find(Long id);

    List<RecipeDto> findAll();

    RecipeDto update(Long id, RecipeDto recipeDto);

}
