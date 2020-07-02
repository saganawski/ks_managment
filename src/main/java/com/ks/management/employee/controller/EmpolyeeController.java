package com.ks.management.employee.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ks.management.employee.Employee;

@RestController
@RequestMapping("/employees")
public class EmpolyeeController {
	
	@PostMapping()
	public Employee createNewEmployee(@Valid Employee employee) {
		System.out.println("hits");
		System.out.println(employee);
		return null;
	}

}
