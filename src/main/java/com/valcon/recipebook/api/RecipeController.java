package com.valcon.recipebook.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.valcon.recipebook.model.dto.RecipeDto;
import com.valcon.recipebook.services.RecipeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {
    private final RecipeService service;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.find(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RecipeDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    @PostMapping
    public ResponseEntity<RecipeDto> create(@RequestBody @Valid RecipeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> update(@PathVariable Long id, @RequestBody @Valid RecipeDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
