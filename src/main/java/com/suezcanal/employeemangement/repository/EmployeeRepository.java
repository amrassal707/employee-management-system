package com.suezcanal.employeemangement.repository;

import com.suezcanal.employeemangement.model.Employee;
import com.suezcanal.employeemangement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    int countByDepartment(Department department);
}
