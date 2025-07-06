package com.suezcanal.employeemangement.service;

import com.suezcanal.employeemangement.dto.EmployeeDTO;
import com.suezcanal.employeemangement.model.Employee;
import com.suezcanal.employeemangement.model.Department;
import com.suezcanal.employeemangement.repository.EmployeeRepository;
import com.suezcanal.employeemangement.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.suezcanal.employeemangement.dto.DepartmentDTO;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional(readOnly = true)
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists: " + employeeDTO.getEmail());
        }
        Department department = departmentRepository.findByName(employeeDTO.getDepartment().getName()).orElseThrow(() -> new EntityNotFoundException("Department not found with name: " + employeeDTO.getDepartment().getName()));
        Employee employee = convertToNewEntity(employeeDTO);
        employee.setDepartment(department);
        employee = employeeRepository.save(employee);
        return convertToDTO(employee);
    }

    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        if (!employeeRepository.existsByEmail(employeeDTO.getEmail()) || !existingEmployee.getEmail().equals(employeeDTO.getEmail())) {
            throw new DataIntegrityViolationException("Email can't be updated: " + employeeDTO.getEmail());
        }
        Department department = departmentRepository.findByName(employeeDTO.getDepartment().getName())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + employeeDTO.getDepartment().getId()));
        existingEmployee = convertToExistingEntity(employeeDTO, existingEmployee);
        existingEmployee.setDepartment(department);
        existingEmployee = employeeRepository.save(existingEmployee);
        return convertToDTO(existingEmployee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto, "department");
        if (employee.getDepartment() != null) {
            DepartmentDTO deptDTO = new DepartmentDTO();
            deptDTO.setId(employee.getDepartment().getId());
            deptDTO.setName(employee.getDepartment().getName());
            dto.setDepartment(deptDTO);
        }
        return dto;
    }

    private Employee convertToNewEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee, "department", "id");
        return employee;
    }

    private Employee convertToExistingEntity(EmployeeDTO dto,Employee employee) {
        BeanUtils.copyProperties(dto, employee, "department", "id");
        return employee;
    }
}
