package com.suezcanal.employeemangement.scheduler;

import com.suezcanal.employeemangement.service.DepartmentSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SummaryScheduler {

    private final DepartmentSummaryService departmentSummaryService;


    @Scheduled(cron = "0 0 9 * * *") // Runs at 9:00 AM every day
    public void generateDailySummary() {
        log.info("Daily summary generation started at {}", System.currentTimeMillis());
        departmentSummaryService.generateDailyDepartmentSummary();
        log.info("Daily summary generation completed at {}", System.currentTimeMillis());
    }
}
