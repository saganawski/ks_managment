package com.ks.management.office;

import java.util.List;

import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ks.management.office.service.OfficeService;

@RestController
@RequestMapping("/offices")
public class OfficeController {
	@Autowired
	private OfficeService officeService;
	
	@GetMapping()
	public List<Office> getOffices(){
		return officeService.getOffices();
	}

	@PostMapping()
	public Office createOffice(@RequestBody Office office, @AuthenticationPrincipal UserPrincipal userPrincipal){
		return officeService.createOffice(office,userPrincipal);
	}

	@GetMapping("{officeId}")
	public Office getOfficeById(@PathVariable("officeId") Integer officeId){
		return officeService.getOfficeById(officeId);
	}

	@PutMapping()
	public Office updateOffice(@RequestBody Office office, @AuthenticationPrincipal UserPrincipal userPrincipal){
		return officeService.updateOffice(office,userPrincipal);
	}
}
