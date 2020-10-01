package com.ks.management.report.dao;

import com.ks.management.report.ScheduleAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaScheduleAudit extends JpaRepository<ScheduleAudit, Integer> {
    List<ScheduleAudit> findAllByIsPayroll(boolean isPayroll);
}
