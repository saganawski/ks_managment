package com.ks.management.report.service;

import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleRepo;
import com.ks.management.report.ScheduleAudit;
import com.ks.management.report.dao.JpaScheduleAudit;
import com.ks.management.report.ui.PayrollDto;
import com.ks.management.security.UserPrincipal;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        final Integer officeId = scheduleAudit.getOffice().getId();
        final LocalDate startDate = scheduleAudit.getStartDate();
        final LocalDate endDate = scheduleAudit.getEndDate();

        final List<EmployeeSchedule> employeeSchedules = jpaEmployeeScheduleRepo.findAllByOfficeForTimePeriod(officeId,startDate.atStartOfDay(),endDate.atStartOfDay());

        scheduleAudit.setEmployeeSchedules(employeeSchedules);

        return jpaScheduleAudit.save(scheduleAudit);
    }

    @Override
    public List<PayrollDto> getScheduleAuditPayrollById(int scheduleAuditId) {
        final List<Object[]> payrollAudits = jpaScheduleAudit.findAllPayrollSums(scheduleAuditId);
        final List<PayrollDto> dtos =  payrollAudits.stream()
                .map(pr -> {
                    final String fistName = (String) Optional.ofNullable(pr[0]).orElse("");
                    final String lastName = (String) Optional.ofNullable(pr[1]).orElse("");
                    final BigDecimal payRate = (BigDecimal) Optional.ofNullable(pr[2]).orElse(new BigDecimal(0.0));
                    final BigDecimal totalOvertimeMinutes = (BigDecimal)Optional.ofNullable(pr[3]).orElse(new BigDecimal(0.0));
                    final BigDecimal totalMileage = (BigDecimal) Optional.ofNullable(pr[4]).orElse(new BigDecimal(0.0));
                    final BigDecimal totalPay = (BigDecimal) Optional.ofNullable(pr[5]).orElse(new BigDecimal(0.0));
                    final BigDecimal totalMinutes = (BigDecimal) Optional.ofNullable(pr[6]).orElse(new BigDecimal(0.0));

                    final BigDecimal overtimeRate = PayrollDto.calculateOvertimeRate(totalOvertimeMinutes,payRate);
                    final BigDecimal regularHours = PayrollDto.calculateRegularHours(totalMinutes,totalOvertimeMinutes);
                    final BigDecimal hourlyPay = PayrollDto.calculateHourlyPay(payRate, regularHours);
                    final BigDecimal overtimeHours = PayrollDto.calculateOvertimeHours(totalOvertimeMinutes);
                    final BigDecimal overtimePay = PayrollDto.calculateOvertimePay(overtimeRate,overtimeHours);
                    final BigDecimal mileagePay = PayrollDto.calculateMileagePay(totalMileage);


                    final PayrollDto dto = PayrollDto.builder()
                            .firstName(fistName)
                            .lastName(lastName)
                            .regularHours(regularHours)
                            .hourlyRate(payRate)
                            .hourlyPay(hourlyPay)
                            .overtimeRate(overtimeRate)
                            .overtimeHours(overtimeHours)
                            .overtimePay(overtimePay)
                            .miles(totalMileage)
                            .mileagePay(mileagePay)
                            .totalPay(totalPay)
                            .build();

                    return dto;
                })
                .collect(Collectors.toList());

        return dtos;
    }
}
