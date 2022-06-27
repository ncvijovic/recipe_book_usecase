package com.valcon.recipebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valcon.recipebook.model.dao.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
