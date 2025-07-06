package com.suezcanal.employeemangement.service;

import com.suezcanal.employeemangement.dto.DepartmentDTO;
import com.suezcanal.employeemangement.dto.EmployeeDTO;
import com.suezcanal.employeemangement.model.Department;
import com.suezcanal.employeemangement.model.Employee;
import com.suezcanal.employeemangement.repository.DepartmentRepository;
import com.suezcanal.employeemangement.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private Department department;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employee.setHireDate(LocalDate.now());
        employee.setSalary(50000.0);
        department = new Department();
        department.setId(1L);
        department.setName("IT");
        employee.setDepartment(department);

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(1L);
        departmentDTO.setName("IT");
        employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        employeeDTO.setHireDate(LocalDate.now());
        employeeDTO.setSalary(50000.0);

        employeeDTO.setDepartment(departmentDTO);
    }

    @Test
    void createEmployee_Success() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO result = employeeService.createEmployee(employeeDTO);

        assertNotNull(result);
        assertEquals(employee.getFirstName(), result.getFirstName());
        assertEquals(employee.getEmail(), result.getEmail());
        assertEquals(employee.getDepartment().getId(), result.getDepartment().getId());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void createEmployee_DuplicateEmail() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class,
                () -> employeeService.createEmployee(employeeDTO));

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        assertEquals(employee.getEmail(), result.getEmail());
        assertEquals(employee.getDepartment().getId(), result.getDepartment().getId());
    }

    @Test
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> employeeService.getEmployeeById(1L));
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeDTO.getDepartment().setName("HR");
        EmployeeDTO result = employeeService.updateEmployee(1L, employeeDTO);

        assertNotNull(result);
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_NotFound() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));

        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
