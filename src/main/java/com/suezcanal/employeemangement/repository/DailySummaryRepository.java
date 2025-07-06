package com.suezcanal.employeemangement.repository;

import com.suezcanal.employeemangement.model.DailySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
}
