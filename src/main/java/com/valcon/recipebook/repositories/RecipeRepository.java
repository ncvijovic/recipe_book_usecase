package com.valcon.recipebook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.valcon.recipebook.model.dao.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
