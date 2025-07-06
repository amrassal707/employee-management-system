package com.suezcanal.employeemangement.service;

import com.suezcanal.employeemangement.dto.DepartmentDTO;
import com.suezcanal.employeemangement.model.Department;
import com.suezcanal.employeemangement.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private DepartmentDTO departmentDTO;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("IT");

        departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setName("IT");
    }

    @Test
    void getAllDepartments_ReturnsList() {
        when(departmentRepository.findAll()).thenReturn(Collections.singletonList(department));
        List<DepartmentDTO> result = departmentService.getAllDepartments();
        assertEquals(1, result.size());
        assertEquals("IT", result.getFirst().getName());
    }

    @Test
    void getDepartmentById_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        DepartmentDTO result = departmentService.getDepartmentById(1L);
        assertNotNull(result);
        assertEquals("IT", result.getName());
    }

    @Test
    void getDepartmentById_NotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> departmentService.getDepartmentById(1L));
    }

    @Test
    void createDepartment_Success() {
        when(departmentRepository.existsByNameIgnoreCase("IT")).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        assertDoesNotThrow(() -> departmentService.createDepartment(department));
    }

    @Test
    void createDepartment_DuplicateName() {
        when(departmentRepository.existsByNameIgnoreCase("IT")).thenReturn(true);
        assertThrows(DataIntegrityViolationException.class, () -> departmentService.createDepartment(department));
        verify(departmentRepository, never()).save(any(Department.class));
    }
    @Test
    void updateDepartment_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        assertDoesNotThrow(() -> departmentService.updateDepartment(1L,departmentDTO));
    }

    @Test
    void deleteDepartment_Success() {
        Department department = new Department();
        department.setId(1L);
        department.setName("IT");
        department.setEmployees(Collections.emptyList());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        assertDoesNotThrow(() -> departmentService.deleteDepartment(1L));
    }

    @Test
    void deleteDepartment_notFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> departmentService.deleteDepartment(1L));
    }

}
