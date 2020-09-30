package com.ks.management.report.controller;

import com.ks.management.employee.Employee;
import com.ks.management.report.ScheduleAudit;
import com.ks.management.report.service.ScheduleAuditService;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheduleAudits")
public class ScheduleAuditController {
	@Autowired
	private ScheduleAuditService scheduleAuditService;

	@GetMapping()
	public List<ScheduleAudit> getScheduleAudits(){
		return scheduleAuditService.getScheduleAudits();
	}

	@PostMapping()
	public ScheduleAudit createScheduleAudit(@RequestBody ScheduleAudit scheduleAudit, @AuthenticationPrincipal UserPrincipal userPrincipal){
		return scheduleAuditService.createScheduleAudit(scheduleAudit,userPrincipal);
	}

	@GetMapping("/{scheduleAuditId}")
	public ScheduleAudit getScheduleAudit(@PathVariable("scheduleAuditId") int scheduleAuditId){
		return scheduleAuditService.getScheduleAuditById(scheduleAuditId);
	}


}
