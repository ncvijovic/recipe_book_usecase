package com.valcon.recipebook.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.valcon.recipebook.config.AbstractFuncTest;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.repositories.IngredientRepository;

class IngredientServiceFuncTest extends AbstractFuncTest {

    @Autowired
    private IngredientService service;

    @Autowired
    private IngredientRepository repository;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    void createIngredientTest() {
        IngredientDto ingredient = IngredientDto.builder().name("salt").build();
        IngredientDto result = service.save(ingredient);

        assertThat(result.name()).isEqualTo(ingredient.name());
        assertThat(result.id()).isNotNull();
    }

    @Test
    void updateIngredientTest() {
        IngredientDto ingredient = IngredientDto.builder().name("salt").build();
        IngredientDto saved = service.save(ingredient);

        IngredientDto result = service.update(saved.id(), IngredientDto.builder().name("pink salt").build());

        assertThat(result.name()).isEqualTo("pink salt");
        assertThat(result.id()).isNotNull();
    }

    @Test
    void deleteIngredientTest() {
        IngredientDto ingredient = IngredientDto.builder().name("salt").build();
        IngredientDto saved = service.save(ingredient);

        service.delete(saved.id());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> service.find(saved.id()));
    }

    @Test
    void findAllTest() {
        IngredientDto salt = IngredientDto.builder().name("salt").build();
        service.save(salt);
        IngredientDto sugar = IngredientDto.builder().name("sugar").build();
        service.save(sugar);

        List<IngredientDto> results = service.findAll();

        assertThat(results.size()).isEqualTo(2);
        assertThat(results.stream().map(IngredientDto::name).toList()).containsExactlyInAnyOrder("salt", "sugar");
    }
}
