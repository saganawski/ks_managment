package com.ks.management.employee.employeeSchedulePayRoll.dao;

import com.ks.management.employee.employeeSchedulePayRoll.EmployeeSchedulePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmployeeSchedulePayroll extends JpaRepository<EmployeeSchedulePayroll,Integer> {
}
