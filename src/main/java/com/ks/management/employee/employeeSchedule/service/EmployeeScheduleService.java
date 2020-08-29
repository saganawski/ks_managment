package com.ks.management.employee.employeeSchedule.service;

import com.ks.management.employee.Employee;
import com.ks.management.security.UserPrincipal;

public interface EmployeeScheduleService {
    Employee createScheduleEmployee(Integer employeeId, String scheduleDate, UserPrincipal userPrincipal);
}
