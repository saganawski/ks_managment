package com.ks.management.position.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ks.management.position.Position;
import com.ks.management.position.service.PositionService;

@RestController
@RequestMapping("/positions")
public class PositionController {
	
	
	@Autowired
	private PositionService positionService;
	
	@GetMapping()
	public List<Position> getPositions(){
		return positionService.getPositions();
	}
	
	@GetMapping("/{name}")
	public Position getPositionByName(@PathVariable String name) {
		return positionService.getPositionByName(name);
	}

}
