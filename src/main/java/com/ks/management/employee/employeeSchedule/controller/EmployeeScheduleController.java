package com.ks.management.employee.employeeSchedule.controller;

import com.ks.management.employee.Employee;
import com.ks.management.employee.employeeSchedule.service.EmployeeScheduleService;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/employees/{employeeId}/schedules")
public class EmployeeScheduleController {
	@Autowired
	private EmployeeScheduleService employeeScheduleService;

	
	@PostMapping()
	public Employee createNewEmployee(@PathVariable("employeeId") Integer employeeId, @RequestBody List<LocalDateTime> scheduleDates, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		return employeeScheduleService.createScheduleEmployee(employeeId,scheduleDates,userPrincipal);
	}



}
