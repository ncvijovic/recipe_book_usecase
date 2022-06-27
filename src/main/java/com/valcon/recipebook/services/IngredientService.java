package com.valcon.recipebook.services;

import java.util.List;

import com.valcon.recipebook.model.dto.IngredientDto;

public interface IngredientService {

    IngredientDto save(IngredientDto ingredientDto);

    void delete(Long id);

    IngredientDto find(Long id);

    List<IngredientDto> findAll();

    IngredientDto update(Long id, IngredientDto ingredientDto);

}
