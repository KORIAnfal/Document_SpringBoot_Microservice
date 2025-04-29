package com.example.document.controller;

import com.example.document.model.DocumentCategory;
import com.example.document.repository.DocumentCategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class DocumentCategoryController {

    private final DocumentCategoryRepository repository;

    public DocumentCategoryController(DocumentCategoryRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public List<DocumentCategory> getAllCategories() {
        return repository.findAll();
    }


    @PostMapping("/create")
    public ResponseEntity<DocumentCategory> createCategory(@RequestBody DocumentCategory category) {
        return ResponseEntity.ok(repository.save(category));
    }


    @PutMapping("/edit/{id}")
    public ResponseEntity<DocumentCategory> updateCategory(@PathVariable Long id, @RequestBody DocumentCategory updatedCategory) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updatedCategory.getName());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
