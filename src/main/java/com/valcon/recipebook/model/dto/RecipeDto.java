package com.valcon.recipebook.model.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "See the model for the proper field value formats", example = """
        {
            // ommit for POST and PUT requests
            "id": 0,
            "name": "crepes",
            "servings": 4,
            "vegetarian": false,
            "creationDate": "26-06-2022 14:36",
            "cookingInstructions": "mix it up",
            "ingredients": [
              {
                // if id is present application will try to bind the ingredient by id to recipe. If the id is wrong the operation will fail
                "id": 0,
                "name": "eggs milk and flour",
                "unit": "handful",
                "amount": 1
              }
            ]
          }""")
public record RecipeDto(@Schema(description = "not needed for POST and PUT") Long id,
                        @NotNull String name,
                        @NotNull Integer servings,
                        @NotNull Boolean vegetarian,
                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
                        @Schema(pattern = "dd-MM-yyyy HH:mm", example = "26-06-2022 14:36")
                        LocalDateTime creationDate,
                        @NotEmpty String cookingInstructions,
                        @Valid Set<IngredientDto> ingredients) {
    @Builder
    public RecipeDto {}
}
