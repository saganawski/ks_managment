package com.ks.management.position.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ks.management.position.Position;

public interface JpaPositionRepo extends JpaRepository<Position, Integer> {
	Position findByCode(String code);

	Position findByName(String name); 
}
