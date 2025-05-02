package com.example.document.controller;

import com.example.document.model.Department;
import com.example.document.repository.DepartmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Create a department
    @PostMapping("/create")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department saved = departmentRepository.save(department);
        return ResponseEntity.ok(saved);
    }

    // Edit a department
    @PutMapping("/edit/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Department existing = departmentRepository.findById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setName(department.getName());
        Department updated = departmentRepository.save(existing);
        return ResponseEntity.ok(updated);
    }

    // Delete a department
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) return ResponseEntity.notFound().build();
        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // List all departments
    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
