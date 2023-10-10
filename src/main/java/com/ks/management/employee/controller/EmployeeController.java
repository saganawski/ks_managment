package com.ks.management.employee.controller;

import com.ks.management.employee.Employee;
import com.ks.management.employee.service.EmployeeService;
import com.ks.management.employee.ui.EditEmployeeDTO;
import com.ks.management.employee.ui.EmployeeDTO;
import com.ks.management.employee.ui.NewEmployeeDTO;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping()
	public  Employee createNewEmployee(@RequestBody NewEmployeeDTO employee, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		return employeeService.createEmployee(employee,userPrincipal);
	}

	@GetMapping()
	public ResponseEntity<Map<String,Object>> getEmployees(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue ="0") int draw,
			@RequestParam(defaultValue = "firstName, lastName") List<String> sortFields,
			@RequestParam(defaultValue = "asc") String sortDirection,
			@RequestParam(defaultValue = "") String search){

		final Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		final Page<Employee> employeePage = employeeService.getAllEmployees(pageable, sortFields, sortDirection, search);
		List<Employee> employeeList = employeePage.getContent();

		final HashMap<String, Object> response = new HashMap<>();
		response.put("data", employeeList);
		response.put("draw", draw);
		response.put("recordsTotal", employeePage.getTotalElements());
		response.put("recordsFiltered", employeePage.getTotalElements());
		response.put("totalPages", employeePage.getTotalPages());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/non-canvassers")
	public Set<Employee> getEmployeesNonCanvassers(){
		return employeeService.getAllEmployeesNonCanvassers();
	}

	@GetMapping("/{employeeId}")
	public Employee getEmployee(@PathVariable("employeeId") Integer employeeId) {
		return employeeService.getEmployee(employeeId);
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

	@DeleteMapping("/{employeeId}/notes/{noteId}")
	public void deleteNotes(@PathVariable("employeeId") int employeeId, @PathVariable("noteId") int noteId){
		employeeService.deleteNote(noteId);
	}

	@GetMapping("/{employeeId}/employeeDTO")
	public EmployeeDTO getEmployeeDtoDetails(@PathVariable("employeeId") int employeeId){
		return employeeService.getEmployeeDTO(employeeId);
	}

}
