package com.suezcanal.employeemangement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email
    private String email;
    
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String phoneNumber;
    
    @NotNull(message = "Salary is required")
    @Min(0)
    private Double salary;
    
    @NotNull(message = "Department is required")
    private DepartmentDTO department;
    
}
