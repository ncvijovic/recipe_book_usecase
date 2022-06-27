package com.valcon.recipebook.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.valcon.recipebook.exceptions.ErrorObject;
import com.valcon.recipebook.model.dto.RecipeDto;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(description = """
                This application allows for CRUD management of recipes. It stores the provided recipes in a database
                which is searchable by the provided id or all recipes may be returned. Be aware that the application
                is protected by basic auth so use credentials in the authorize section of this documentation""",
                     contact = @Contact(name = "Nebojsa Cvijovic", email = "nebojsa.cvijovic@valcon.com"),
                     title = "Recipe Book Use Case"),
        security = @SecurityRequirement(name = "basicAuth")
)
public interface RecipeApi {

    @Operation(summary = "Returns a recipe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe with given id returned"),
            @ApiResponse(responseCode = "404", description = "Recipe with given id not found"
                    , content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    ResponseEntity<RecipeDto> findById(@PathVariable Long id);

    @Operation(summary = "Returns all recipes")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Returns a list of all recipes or empty list if no recipes exist")
    )
    ResponseEntity<List<RecipeDto>> findAll();

    @Operation(summary = "Save a new recipe",
               description = "Sending a recipe without an id and with ingredients existing or not will create a new recipe")
    @ApiResponses(
            @ApiResponse(responseCode = "201", description = """
                    If the sent recipe holds ingredients with ids it will use those existing ingredient, provided they are found by name.
                    Otherwise it will create new ingredients before creating the recipe""")
    )
    ResponseEntity<RecipeDto> create(@RequestBody RecipeDto dto);

    @Operation(summary = "Update an existing recipe")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = """
            Updates the existing recipe with with the provided payload. This is a full update so all ingredients will be replaced.
            Any fields sent as null will be removed from the recipe."""),
            @ApiResponse(responseCode = "404", description = "Returned if the recipe with provided id could not be found."
                    , content = @Content(schema = @Schema(implementation = ErrorObject.class)))
    })
    ResponseEntity<RecipeDto> update(@PathVariable Long id, @RequestBody RecipeDto dto);

    @Operation(summary = "Delete a recipe by id")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "Deletes a recipe with the provided id.")
    )
    ResponseEntity<Void> deleteById(@PathVariable Long id);
}
