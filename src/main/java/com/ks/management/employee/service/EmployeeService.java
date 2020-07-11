package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(NewEmployeeDTO Employee);

    List<Employee> getAllEmployees();

    Employee getEmployee(Integer employeeId);

    Employee updateEmployee(EditEmployeeDTO employeeDTO);

    void deleteEmployee(Integer employeeId);
}
