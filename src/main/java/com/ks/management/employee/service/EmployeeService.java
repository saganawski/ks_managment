package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(NewEmployeeDTO Employee, UserPrincipal userPrincipal);

    List<Employee> getAllEmployees();

    Employee getEmployee(Integer employeeId);

    Employee updateEmployee(EditEmployeeDTO employeeDTO, UserPrincipal userPrincipal);

    void deleteEmployee(Integer employeeId);

    Employee createNewEmployee(Employee employee, UserPrincipal userPrincipal);
}
