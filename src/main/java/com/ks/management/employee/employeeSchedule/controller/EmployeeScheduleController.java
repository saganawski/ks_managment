package com.ks.management.employee.employeeSchedule.controller;

import com.ks.management.employee.Employee;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.service.EmployeeScheduleService;
import com.ks.management.office.Office;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeScheduleController {
	@Autowired
	private EmployeeScheduleService employeeScheduleService;

	@GetMapping("/schedules/office/{officeId}")
	public List<EmployeeSchedule> getEmployeeSchedulesByOffice(@PathVariable("officeId") Integer officeId){
		return  employeeScheduleService.getEmployeeSchedulesByOffice(officeId);
	}

	@GetMapping("/{employeeId}/schedules")
	public List<EmployeeSchedule> getSchedule(@PathVariable("employeeId") Integer employeeId){
		return employeeScheduleService.getSchedules(employeeId);
	}
	@PostMapping("/{employeeId}/schedules")
	public Employee createNewEmployee(@PathVariable("employeeId") Integer employeeId, @RequestBody List<LocalDateTime> scheduleDates, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		return employeeScheduleService.createScheduleEmployee(employeeId,scheduleDates,userPrincipal);
	}

	@DeleteMapping("/{employeeId}/schedules/{employeeScheduleId}")
	void deleteEmployeeScheduleById(@PathVariable("employeeId") Integer employeeId, @PathVariable("employeeScheduleId") Integer employeeScheduleId){
		employeeScheduleService.deleteEmployeeScheduleById(employeeScheduleId);
	}

}
