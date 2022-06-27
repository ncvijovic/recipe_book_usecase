package com.valcon.recipebook.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.valcon.recipebook.model.dao.Ingredient;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.model.mappers.IngredientMapper;
import com.valcon.recipebook.repositories.IngredientRepository;

@ExtendWith(MockitoExtension.class)
class IngredientServiceUnitTest {

    @InjectMocks
    private IngredientServiceImpl ingredientService;
    @Mock
    private IngredientRepository repository;
    @Mock
    private IngredientMapper mapper;

    @Test
    void saveIngredientTest() {
        IngredientDto dto = createIngredientDto(null);
        Ingredient ingredient = createIngredient();

        when(mapper.map(dto)).thenReturn(ingredient);
        when(mapper.map(ingredient)).thenCallRealMethod();
        when(repository.save(ingredient)).thenReturn(ingredient);

        IngredientDto result = ingredientService.save(dto);
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("salt");
    }

    @Test
    void updateIngredientTest() {
        IngredientDto dto = createIngredientDto(1L);
        Ingredient ingredient = createIngredient();

        when(repository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(repository.save(ingredient)).thenReturn(ingredient);
        when(mapper.map(dto)).thenReturn(ingredient);
        when(mapper.map(ingredient)).thenCallRealMethod();

        IngredientDto result = ingredientService.update(1L, dto);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("salt");
    }

    @Test
    void deleteIngredientTest() {
        doNothing().when(repository).deleteById(1L);

        ingredientService.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void findIngredientTest() {
        IngredientDto dto = createIngredientDto(1L);
        Ingredient ingredient = createIngredient();

        when(repository.findById(1L)).thenReturn(Optional.of(ingredient));
        when(mapper.map(ingredient)).thenReturn(dto);

        IngredientDto result = ingredientService.find(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("salt");
    }

    @Test
    void findAllIngredientsTest() {
        IngredientDto dto = createIngredientDto(1L);
        Ingredient ingredient = createIngredient();

        when(repository.findAll()).thenReturn(Lists.list(ingredient));
        when(mapper.map(ingredient)).thenReturn(dto);

        List<IngredientDto> results = ingredientService.findAll();

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).id()).isEqualTo(1L);
        assertThat(results.get(0).name()).isEqualTo("salt");
    }

    private Ingredient createIngredient() {
        return Ingredient.builder().name("salt").id(1L).build();
    }

    private IngredientDto createIngredientDto(Long id) {
        return IngredientDto.builder().id(id).name("salt").build();
    }
}
