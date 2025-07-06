package com.suezcanal.employeemangement.service;

import com.suezcanal.employeemangement.dto.DepartmentDTO;
import com.suezcanal.employeemangement.model.Department;
import com.suezcanal.employeemangement.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream().sorted(Department::compareTo).map(this::toDTO).toList();
    }




    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    @Transactional
    public void createDepartment(Department department) {
        if (departmentRepository.existsByNameIgnoreCase(department.getName())) {
            throw new DataIntegrityViolationException("Department name already exists: " + department.getName());
        }
        departmentRepository.save(department);
    }

    @Transactional
    public void updateDepartment(Department department) {
        log.info("Updating department: {}", department);
        if(departmentRepository.existsByNameIgnoreCase(department.getName())) {
            throw new DataIntegrityViolationException("Department name already exists: " + department.getName());
        }
        departmentRepository.save(department);
    }

    private DepartmentDTO toDTO(Department department) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    public Department checkDepartmentExists(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
    }

    public void deleteDepartment(Long id) {
        Department department = checkDepartmentExists(id);
        if (!department.getEmployees().isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete department with existing employees");
        }

        departmentRepository.delete(department);
    }
}
