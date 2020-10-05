package com.ks.management.report.dao;

import com.ks.management.report.ScheduleAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaScheduleAudit extends JpaRepository<ScheduleAudit, Integer> {
    List<ScheduleAudit> findAllByIsPayroll(boolean isPayroll);

    @Query(value = "SELECT e.first_name, e.last_name, esp.pay_rate, sum(esp.overtime_minutes) AS total_overtime_minutes, " +
            "sum(esp.mileage) AS total_mileage, sum(esp.total_day_wage) AS total_pay, sum(esp.total_minutes) AS total_minutes " +
            "FROM schedule_audit_employee_schedule AS saes " +
            "JOIN employee_schedule AS es ON es.id = saes.employee_schedule_id " +
            "JOIN employee_schedule_payroll AS esp ON esp.id = es.employee_schedule_payroll_id " +
            "JOIN employee AS e ON e.id = es.employee_id " +
            "WHERE saes.schedule_audit_id = ?1 " +
            "GROUP BY es.employee_id, esp.pay_rate ",
            nativeQuery = true)
    List<Object[]> findAllPayrollSums(int scheduleAuditId);
}
