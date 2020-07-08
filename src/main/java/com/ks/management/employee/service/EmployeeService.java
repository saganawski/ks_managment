package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.ui.NewEmployeeDTO;

public interface EmployeeService {

    Employee createEmployee(NewEmployeeDTO Employee);
}
