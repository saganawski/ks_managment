package com.ks.management.employee.employeeSchedule.controller;

import com.ks.management.employee.Employee;
import com.ks.management.employee.employeeSchedule.service.EmployeeScheduleService;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/employees/{employeeId}/schedules/")
public class EmployeeScheduleController {
	@Autowired
	private EmployeeScheduleService employeeScheduleService;

	@PostMapping("{scheduledDate}")
	public Employee createNewEmployee(@PathVariable("employeeId") Integer employeeId, @PathVariable("scheduledDate") String scheduleDate, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		return employeeScheduleService.createScheduleEmployee(employeeId,scheduleDate,userPrincipal);
	}



}
