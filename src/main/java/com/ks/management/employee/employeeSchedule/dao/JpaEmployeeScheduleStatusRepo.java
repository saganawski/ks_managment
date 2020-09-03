package com.ks.management.employee.employeeSchedule.dao;

import com.ks.management.employee.employeeSchedule.EmployeeScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmployeeScheduleStatusRepo extends JpaRepository<EmployeeScheduleStatus, Integer> {
}
