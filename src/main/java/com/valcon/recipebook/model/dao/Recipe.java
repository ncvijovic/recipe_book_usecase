package com.valcon.recipebook.model.dao;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
@Table(name = "recipes")
public class Recipe {

    public static final String GENERATOR = "recipe_seq_generator";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GENERATOR)
    @SequenceGenerator(name = GENERATOR, sequenceName = "recipe_id_sequence", allocationSize = 1)
    private Long id;

    private String name;

    private Integer servings;

    private Boolean vegetarian;

    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    private String cookingInstructions;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<IngredientRecipe> ingredientRecipes = new HashSet<>();

    public void addIngredients(Set<IngredientRecipe> ingredients) {
        ingredientRecipes.clear();
        ingredientRecipes.addAll(ingredients);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Recipe that)) return false;
        return Objects.equals(this.id, that.id);
    }

    public int hashCode() {
        return Objects.hashCode(id);
    }
}
