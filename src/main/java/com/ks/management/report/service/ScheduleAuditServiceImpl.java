package com.ks.management.report.service;

import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleRepo;
import com.ks.management.report.ScheduleAudit;
import com.ks.management.report.dao.JpaScheduleAudit;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ScheduleAuditServiceImpl implements ScheduleAuditService {
    @Autowired
    private JpaScheduleAudit jpaScheduleAudit;
    @Autowired
    private JpaEmployeeScheduleRepo jpaEmployeeScheduleRepo;

    @Override
    public List<ScheduleAudit> getScheduleAudits() {
        return jpaScheduleAudit.findAll();
    }

    @Override
    public ScheduleAudit createScheduleAudit(ScheduleAudit scheduleAudit, UserPrincipal userPrincipal) {
        final Integer userId = userPrincipal.getUserId();

        scheduleAudit.setCreatedBy(userId);
        scheduleAudit.setUpdatedBy(userId);

        final Integer officeId = scheduleAudit.getOffice().getId();
        final LocalDate startDate = scheduleAudit.getStartDate();
        final LocalDate endDate = scheduleAudit.getEndDate();

        final List<EmployeeSchedule> employeeSchedules = jpaEmployeeScheduleRepo.findAllByOfficeForTimePeriod(officeId,startDate.atStartOfDay(),endDate.atStartOfDay());

        scheduleAudit.setEmployeeSchedules(employeeSchedules);

        return jpaScheduleAudit.save(scheduleAudit);
    }

    @Override
    public ScheduleAudit getScheduleAuditById(int scheduleAuditId) {
        return jpaScheduleAudit.getOne(scheduleAuditId);
    }

    @Override
    public void deleteScheduleAuditById(int scheduleAuditId) {
        jpaScheduleAudit.deleteById(scheduleAuditId);
    }

    @Override
    public List<ScheduleAudit> getScheduleAuditsPayroll() {
        return jpaScheduleAudit.findAllByIsPayroll(true);
    }

    @Override
    public ScheduleAudit createScheduleAuditPayroll(ScheduleAudit scheduleAudit, UserPrincipal userPrincipal) {
        final Integer userId = userPrincipal.getUserId();
        scheduleAudit.setCreatedBy(userId);
        scheduleAudit.setUpdatedBy(userId);
        scheduleAudit.setIsPayroll(true);

        return jpaScheduleAudit.save(scheduleAudit);
    }
}
