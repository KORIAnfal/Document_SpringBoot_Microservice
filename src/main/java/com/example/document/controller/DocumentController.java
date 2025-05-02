package com.example.document.controller;

import com.example.document.dto.CreateDocumentRequest;
import com.example.document.model.DocumentCategory;
import com.example.document.model.Department;
import com.example.document.model.Document;
import com.example.document.repository.DocumentRepository;
import com.example.document.repository.DocumentCategoryRepository;
import com.example.document.repository.DepartmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final DocumentCategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;

    public DocumentController(DocumentRepository documentRepository,
            DocumentCategoryRepository categoryRepository,
            DepartmentRepository departmentRepository) {
        this.documentRepository = documentRepository;
        this.categoryRepository = categoryRepository;
        this.departmentRepository = departmentRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Document> createDocument(@RequestBody CreateDocumentRequest request) {
        DocumentCategory category = (request.getCategoryId() != null)
                ? categoryRepository.findById(request.getCategoryId()).orElse(null)
                : null;

        Department department = (request.getDepartmentId() != null)
                ? departmentRepository.findById(request.getDepartmentId()).orElse(null)
                : null;

        Document document = new Document();
        document.setCategory(category);
        document.setTitle(request.getTitle());
        document.setDepartment(department);
        document.setFilePath(request.getFilePath());

        Document savedDocument = documentRepository.save(document);
        return ResponseEntity.ok(savedDocument);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id,
            @RequestParam Long categoryId,
            @RequestParam String title,
            @RequestParam Long departmentId,
            @RequestParam String filePath) {
        Document document = documentRepository.findById(id).orElse(null);
        if (document == null) {
            return ResponseEntity.notFound().build(); // Return 404 if document not found
        }

        DocumentCategory category = categoryRepository.findById(categoryId).orElse(null);
        Department department = departmentRepository.findById(departmentId).orElse(null);

        if (category != null)
            document.setCategory(category);
        if (department != null)
            document.setDepartment(department);
        document.setTitle(title);
        document.setFilePath(filePath);

        Document updatedDocument = documentRepository.save(document);
        return ResponseEntity.ok(updatedDocument);
    }

    // Delete Document
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        if (documentRepository.existsById(id)) {
            documentRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Return 204 if successfully deleted
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if document not found
        }
    }

    // List All Documents
    @GetMapping
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}
