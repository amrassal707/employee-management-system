package com.suezcanal.employeemangement.configuration;

import com.suezcanal.employeemangement.model.Department;
import com.suezcanal.employeemangement.model.Employee;
import com.suezcanal.employeemangement.model.User;
import com.suezcanal.employeemangement.repository.DepartmentRepository;
import com.suezcanal.employeemangement.repository.EmployeeRepository;
import com.suezcanal.employeemangement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {
        // Create admin user if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
        // Create regular user if not exists
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("USER");
            userRepository.save(user);
        }
        // Create default departments if not exist
        if (departmentRepository.count() == 0) {
            createDepartment("IT");
            createDepartment("HR");
            createDepartment("Finance");
            createDepartment("Operations");
            createDepartment("Marketing");
        }

        // Create default employees if not exist
        if (departmentRepository.count() > 0) {
            createEmployee("Ahmed", "Ahmed@gmail.com", 1L, "Ezz", 5000.0, "01023456567", LocalDate.now(), LocalDate.of(1990, 1, 1));
            createEmployee("Bahaa", "bahaa@gmail.com", 2L, "Assal", 6000.0, "01023456567", LocalDate.now(), LocalDate.of(1992, 2, 2));
            createEmployee("Amr", "amr@gmail.com", 3L, "Ramy", 7000.0, "01023456567", LocalDate.now(), LocalDate.of(1991, 3, 3));
            createEmployee("Mohamed", "Mohammed@gmail.com", 4L, "Ayman", 8000.0, "01023456567", LocalDate.now(), LocalDate.of(1989, 4, 4));
        }
    }

    private void createDepartment(String name) {
        Department department = new Department();
        department.setName(name);
        departmentRepository.save(department);
    }

    private void createEmployee(String name, String email, Long departmentId, String lastName, double salary, String phoneNumber, LocalDate hireDate, LocalDate dateOfBirth) {
        Employee employee = new Employee();
        employee.setFirstName(name);
        employee.setEmail(email);
        employee.setLastName(lastName);
        employee.setSalary(salary);
        employee.setPhoneNumber(phoneNumber);
        employee.setHireDate(hireDate);
        employee.setDateOfBirth(dateOfBirth);
        Department department = departmentRepository.findById(departmentId).orElse(null);
        employee.setDepartment(department);
        employeeRepository.save(employee);
    }
}
