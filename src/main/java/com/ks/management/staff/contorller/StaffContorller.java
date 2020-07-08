package com.ks.management.staff.contorller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ks.management.staff.Staff;
import com.ks.management.staff.service.StaffService;

@RestController
@RequestMapping("/staffs")
public class StaffContorller {
	@Autowired
	private StaffService staffService;
	
	@GetMapping()
	public List<Staff> getStaffs() {
		return staffService.getStaffs();
	}
	
	@PutMapping()
	public Staff updateStaff(@Valid Staff staff) {
		return staffService.update(staff);
	}
	
	@PostMapping()
	public Staff createStaff(@Valid Staff staff) {
		return staffService.create(staff);
	}
}
