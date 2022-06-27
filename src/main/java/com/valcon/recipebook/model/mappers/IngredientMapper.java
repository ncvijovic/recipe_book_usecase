package com.valcon.recipebook.model.mappers;

import org.springframework.stereotype.Component;

import com.valcon.recipebook.model.dao.Ingredient;
import com.valcon.recipebook.model.dto.IngredientDto;

@Component
public class IngredientMapper {

    public Ingredient map(IngredientDto dto) {
        return Ingredient.builder()
                         .id(dto.id())
                         .name(dto.name())
                         .build();
    }

    public IngredientDto map(Ingredient ingredient) {
        return getIngredientDtoBuilder(ingredient).build();
    }

    public IngredientDto map(Ingredient ingredient, Integer amount, String unit) {
        return getIngredientDtoBuilder(ingredient)
                            .amount(amount)
                            .unit(unit)
                            .build();
    }

    private IngredientDto.IngredientDtoBuilder getIngredientDtoBuilder(Ingredient ingredient) {
        return IngredientDto.builder()
                            .id(ingredient.getId())
                            .name(ingredient.getName());
    }
}
