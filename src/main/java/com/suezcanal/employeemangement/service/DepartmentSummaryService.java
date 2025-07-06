package com.suezcanal.employeemangement.service;

import com.suezcanal.employeemangement.model.DailySummary;
import com.suezcanal.employeemangement.model.Department;
import com.suezcanal.employeemangement.repository.DailySummaryRepository;
import com.suezcanal.employeemangement.repository.DepartmentRepository;
import com.suezcanal.employeemangement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentSummaryService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DailySummaryRepository dailySummaryRepository;

    @Transactional
    public void generateDailyDepartmentSummary() {

        departmentRepository.findAll().forEach(department -> {
            int employeeCount = employeeRepository.countByDepartment(department);
            logDepartmentSummary(department, employeeCount);
            saveDailySummary(department, employeeCount);
        });
        
    }

    private void logDepartmentSummary(Department department, int count) {
        log.info("Department: {} - Total Employees: {}", department.getName(), count);
    }

    private void saveDailySummary(Department department, int count) {
        DailySummary summary = new DailySummary();
        summary.setDepartment(department);
        summary.setEmployeeCount(count);
        summary.setTimestamp(LocalDateTime.now());
        dailySummaryRepository.save(summary);
    }
}
