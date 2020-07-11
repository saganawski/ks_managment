package com.ks.management.employee.controller;

import com.ks.management.employee.Employee;
import com.ks.management.employee.service.EmployeeService;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping()
	public Employee createNewEmployee(@RequestBody NewEmployeeDTO employee) {
		return employeeService.createEmployee(employee);
	}

	@GetMapping()
	public List<Employee> getEmployees(){
		return employeeService.getAllEmployees();
	}

	@GetMapping("/{employeeId}")
	public Employee getEmployee(@PathVariable("employeeId") Integer employeeId) {
		return employeeService.
				getEmployee(employeeId);
	}

	@PutMapping("/{employeeId}")
	public Employee updateEmployee(@PathVariable("employeeId") Integer employeeId, @RequestBody EditEmployeeDTO employeeDTO){
		return employeeService.updateEmployee(employeeDTO);
	}

	@DeleteMapping("/{employeeId}")
	public void deleteEmployee(@PathVariable("employeeId") Integer employeeId){
		employeeService.deleteEmployee(employeeId);
	}


}
