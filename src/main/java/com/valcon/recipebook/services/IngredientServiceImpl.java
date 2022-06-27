package com.valcon.recipebook.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.valcon.recipebook.model.dao.Ingredient;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.model.mappers.IngredientMapper;
import com.valcon.recipebook.repositories.IngredientRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository repository;
    private final IngredientMapper mapper;

    @Override
    public IngredientDto save(IngredientDto ingredientDto) {
        log.info("Saving ingredient named {}", ingredientDto.name());
        Ingredient ingredient = mapper.map(ingredientDto);
        return mapper.map(repository.save(ingredient));
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting ingredient with id {}", id);
        repository.deleteById(id);
    }

    @Override
    public IngredientDto find(Long id) {
        return repository.findById(id).map(mapper::map).orElseThrow();
    }

    @Override
    public List<IngredientDto> findAll() {
        return repository.findAll().stream()
                         .map(mapper::map)
                         .toList();
    }

    @Override
    public IngredientDto update(Long id, IngredientDto ingredientDto) {
        log.info("Updating ingredient with id {}", id);
        Ingredient ingredient = repository.findById(id).orElseThrow();
        Ingredient newIngredient = mapper.map(ingredientDto);
        ingredient.setName(newIngredient.getName());
        return mapper.map(repository.save(ingredient));
    }
}
