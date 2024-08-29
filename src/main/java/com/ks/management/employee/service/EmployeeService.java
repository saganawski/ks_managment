package com.ks.management.employee.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.EmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface EmployeeService {

    Employee createEmployee(NewEmployeeDTO Employee, UserPrincipal userPrincipal);

    List<Employee> getAllEmployees();

    Employee getEmployee(Integer employeeId);

    Employee updateEmployee(EditEmployeeDTO employeeDTO, UserPrincipal userPrincipal);

    void deleteEmployee(Integer employeeId, UserPrincipal userPrincipal);

    Employee createNewEmployee(Employee employee, UserPrincipal userPrincipal);

    Boolean checkIfEmployeeExists(String lastName, String email);

    Set<Employee> getAllEmployeesNonCanvassers();

    void deleteNote(int noteId);

    EmployeeDTO getEmployeeDTO(int employeeId);

    Page<Employee> getAllEmployees(Pageable pageable, List<String> sortFields, String sortDirection, String search);
}
