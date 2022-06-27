package com.valcon.recipebook.model.dto;

import javax.validation.constraints.NotNull;

import lombok.Builder;

public record IngredientDto(Long id,
                            @NotNull String name,
                            String unit,
                            Integer amount) {

    @Builder
    public IngredientDto {}
}
