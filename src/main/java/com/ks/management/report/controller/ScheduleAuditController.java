package com.ks.management.report.controller;

import com.ks.management.employee.Employee;
import com.ks.management.report.ScheduleAudit;
import com.ks.management.report.service.ScheduleAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
