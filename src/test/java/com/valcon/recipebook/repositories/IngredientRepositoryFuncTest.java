package com.valcon.recipebook.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.valcon.recipebook.config.AbstractFuncTest;
import com.valcon.recipebook.model.dao.Ingredient;

class IngredientRepositoryFuncTest extends AbstractFuncTest {

    @Autowired
    private IngredientRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    void saveIngredientTest() {
        repository.save(Ingredient.builder().name("salt").build());
        List<Ingredient> ingredients = repository.findAll();
        assertThat(ingredients.size()).isEqualTo(1);
        assertThat(ingredients.get(0).getName()).isEqualTo("salt");

    }
}
