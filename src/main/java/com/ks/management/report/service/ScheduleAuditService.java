package com.ks.management.report.service;

import com.ks.management.report.ScheduleAudit;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface ScheduleAuditService {
    List<ScheduleAudit> getScheduleAudits();

    ScheduleAudit createScheduleAudit(ScheduleAudit scheduleAudit, UserPrincipal userPrincipal);
}
