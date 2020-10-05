package com.ks.management.report.service;

import com.ks.management.report.ScheduleAudit;
import com.ks.management.report.ui.PayrollDto;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface ScheduleAuditService {
    List<ScheduleAudit> getScheduleAudits();

    ScheduleAudit createScheduleAudit(ScheduleAudit scheduleAudit, UserPrincipal userPrincipal);

    ScheduleAudit getScheduleAuditById(int scheduleAuditId, UserPrincipal userPrincipal);

    void deleteScheduleAuditById(int scheduleAuditId);

    List<ScheduleAudit> getScheduleAuditsPayroll();

    ScheduleAudit createScheduleAuditPayroll(ScheduleAudit scheduleAudit, UserPrincipal userPrincipal);

    List<PayrollDto> getScheduleAuditPayrollById(int scheduleAuditId, UserPrincipal userPrincipal);
}
