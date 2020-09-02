package com.ks.management.employee.employeeSchedule.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.security.UserPrincipal;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeScheduleService {
    Employee createScheduleEmployee(Integer employeeId, List<LocalDateTime> scheduleDates, UserPrincipal userPrincipal);

    List<EmployeeSchedule> getSchedules(Integer employeeId);

    void deleteEmployeeScheduleById(Integer employeeScheduleId);
}
