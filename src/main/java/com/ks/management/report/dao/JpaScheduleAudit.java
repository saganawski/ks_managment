package com.ks.management.report.dao;

import com.ks.management.report.ScheduleAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaScheduleAudit extends JpaRepository<ScheduleAudit, Integer> {
}
