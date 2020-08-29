package com.ks.management.employee.employeeSchedule.dao;

import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmployeeScheduleRepo extends JpaRepository<EmployeeSchedule, Integer> {
}
