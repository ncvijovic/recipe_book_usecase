package com.valcon.recipebook.api;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valcon.recipebook.model.dto.IngredientDto;
import com.valcon.recipebook.model.dto.RecipeDto;
import com.valcon.recipebook.services.RecipeService;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@WebMvcTest(controllers = RecipeController.class)
class RecipeControllerFuncTest {

    public final static String PATH = "/api/recipes";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecipeService recipeService;

    @TestConfiguration
    static class DefaultConfigWithoutCsrf extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            super.configure(http);
            http.csrf().disable();
        }
    }

    @Test
    @DisplayName("Test recipe creation")
    @WithMockUser(username = "user", roles = "USER")
    void createRecipeTest() throws Exception {
        // Given
        RecipeDto dto = createRecipeDto();

        //When
        when(recipeService.save(dto)).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(dto)))
        // Then
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(dto.name())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.servings", Matchers.is(dto.servings())));
    }

    @Test
    @DisplayName("Test authentication failure")
    void authenticationFailedTest() throws Exception {
        // Given
        RecipeDto dto = createRecipeDto();

        //When
        when(recipeService.save(dto)).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(dto)))
        // Then
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("Test validation failure")
    @WithMockUser(username = "user", roles = "USER")
    void validationFailedTest() throws Exception {
        // Given
        RecipeDto dto = createRecipeDto(null);

        //When
        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(dto)))
        // Then
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Validation errors occurred")))
               .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Find user by id")
    @WithMockUser(username = "user", roles = "USER")
    void findUserByIdTest() throws Exception {
        // Given
        Long id = 1L;
        RecipeDto dto = createRecipeDto();

        // When
        when(recipeService.find(1L)).thenReturn(dto);
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/" + id))
        // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
    }

    @Test
    @DisplayName("Cannot find recipe by id")
    @WithMockUser(username = "user", roles = "USER")
    void findRecipeByIdNotFound() throws Exception {
        // Given
        Long id = 1L;

        // When
        when(recipeService.find(1L)).thenThrow(new NoSuchElementException());
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/" + id))
               // Then
               .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Find all recipes")
    @WithMockUser(username = "user", roles = "USER")
    void findAllRecipesTest() throws Exception {
        // Given
        RecipeDto dto = createRecipeDto();

        //When
        when(recipeService.findAll()).thenReturn(List.of(dto));
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
        // Then
                .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    @DisplayName("Return empty list when searching for all recipes")
    @WithMockUser(username = "user", roles = "USER")
    void findAllRecipesEmptyListTest() throws Exception {
        // Given
        // Request all recipes

        //When
        when(recipeService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get(PATH))
        // Then
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
               .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    @DisplayName("Delete recipe by id")
    @WithMockUser(username = "user", roles = "USER")
    void deleteRecipe() throws Exception {
        // Given
        Long id = 1L;

        // When
        doNothing().when(recipeService).delete(id);
        mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/" + id))
        // Then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private RecipeDto createRecipeDto() {
        return createRecipeDto("dish");
    }
    private RecipeDto createRecipeDto(String name) {
        return RecipeDto.builder().id(1L).name(name).servings(1).vegetarian(false).cookingInstructions("instructions")
                        .creationDate(LocalDateTime.of(2020, 1, 1, 0, 0)).ingredients(createIngredientsDto()).build();
    }

    private Set<IngredientDto> createIngredientsDto() {
        return Sets.set(IngredientDto.builder().name("salt").unit("pinch").amount(1).build());
    }

}
