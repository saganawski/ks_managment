package com.ks.management.employee.employeeSchedule.controller;

import com.ks.management.employee.Employee;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.EmployeeScheduleStatus;
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

	@GetMapping("/schedules/office/{officeId}/startDate/{startDate}/endDate/{endDate}")
	public List<EmployeeSchedule> getScheduleForOfficeByTimePeriod(@PathVariable("officeId") Integer officeId, @PathVariable("startDate") String startDate,
														   @PathVariable("endDate") String endDate){
		return employeeScheduleService.getEmployeeSchedulesByOfficeForTimePeriod(officeId,startDate,endDate);
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

	@PostMapping("/employee-schedule/{employeeScheduleId}/schedules/status")
	public EmployeeSchedule setEmployeeScheduleStatus(@PathVariable("employeeScheduleId") Integer employeeScheduleId,
													  @RequestBody EmployeeScheduleStatus status,
													  @AuthenticationPrincipal UserPrincipal userPrincipal){
		return employeeScheduleService.setEmployeeScheduleStatus(employeeScheduleId,status,userPrincipal);
	}

	@PostMapping("/employee-schedule/{employeeScheduleId}/status-payroll")
	public EmployeeSchedule setEmployeeScheduleStatusAndPayRoll(@PathVariable("employeeScheduleId") Integer employeeScheduleId,
													  @RequestBody EmployeeSchedule employeeSchedule,
													  @AuthenticationPrincipal UserPrincipal userPrincipal){
		return employeeScheduleService.setEmployeeScheduleStatusAndPayRoll(employeeScheduleId,employeeSchedule,userPrincipal);
	}

}
