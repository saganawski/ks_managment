package com.ks.management.employee.controller;

import com.ks.management.employee.Employee;
import com.ks.management.employee.service.EmployeeService;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping()
	public Employee createNewEmployee(@RequestBody NewEmployeeDTO employee, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		return employeeService.createEmployee(employee,userPrincipal);
	}

	@GetMapping()
	public List<Employee> getEmployees(){
		return employeeService.getAllEmployees();
	}

	@GetMapping("/non-canvassers")
	public Set<Employee> getEmployeesNonCanvassers(){
		return employeeService.getAllEmployeesNonCanvassers();
	}

	@GetMapping("/{employeeId}")
	public Employee getEmployee(@PathVariable("employeeId") Integer employeeId) {
		return employeeService.
				getEmployee(employeeId);
	}

	@PutMapping("/{employeeId}")
	public Employee updateEmployee(@PathVariable("employeeId") Integer employeeId, @RequestBody EditEmployeeDTO employeeDTO, @AuthenticationPrincipal UserPrincipal userPrincipal){
		return employeeService.updateEmployee(employeeDTO,userPrincipal);
	}

	@DeleteMapping("/{employeeId}")
	public void deleteEmployee(@PathVariable("employeeId") Integer employeeId, @AuthenticationPrincipal UserPrincipal userPrincipal){
		employeeService.deleteEmployee(employeeId, userPrincipal);
	}

	@PostMapping("/new")
	public Employee createEmployee(@RequestBody Employee employee,@AuthenticationPrincipal UserPrincipal userPrincipal){
		return  employeeService.createNewEmployee(employee,userPrincipal);
	}

	@GetMapping("/lastname/{lastname}/email/{email}")
	public Boolean checkIfEmployeeExists(@PathVariable("lastname")String lastName, @PathVariable("email")String email){
		return employeeService.checkIfEmployeeExists(lastName,email);
	}

}
