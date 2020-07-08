package com.ks.management.office;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ks.management.office.service.OfficeService;

@RestController
@RequestMapping("/offices")
public class OfficeController {
	@Autowired
	private OfficeService officeService;
	
	@GetMapping()
	public List<Office> getOffices(){
		System.out.println("in controller");
		return officeService.getOffices();
	}
}
