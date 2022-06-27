package com.valcon.recipebook.model.dao;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "ingredient_recipe")
public class IngredientRecipe {

    @EmbeddedId
    private IngredientRecipeId id;

    @ManyToOne
    @MapsId("recipe_id")
    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredient_id")
    private Ingredient ingredient;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "unit")
    private String unit;

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof IngredientRecipe that)) return false;
        return Objects.equals(this.id, that.getId());
    }

    public int hashCode() {
        return Objects.hashCode(id);
    }

}
