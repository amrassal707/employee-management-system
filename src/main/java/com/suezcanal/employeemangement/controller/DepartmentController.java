package com.suezcanal.employeemangement.controller;

import com.suezcanal.employeemangement.dto.DepartmentDTO;
import com.suezcanal.employeemangement.model.Department;
import com.suezcanal.employeemangement.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Operations related to departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "Get all departments", description = "Returns a list of all departments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PostMapping
    @Operation(summary = "Create a new department", description = "Creates a new department record for employees")
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setName(departmentDTO.getName());
        departmentService.createDepartment(department);
        departmentDTO.setId(department.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a department", description = "Updates a department record for employees")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDTO departmentDTO) {
        Department department = departmentService.checkDepartmentExists(id);
        department.setName(departmentDTO.getName());
        departmentService.updateDepartment(department);
        departmentDTO.setId(department.getId());
        return ResponseEntity.status(HttpStatus.OK).body(departmentDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a department", description = "Deletes a department record for employees")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a department by ID", description = "Returns a single department by their ID")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(departmentService.getDepartmentById(id));
    }

}
