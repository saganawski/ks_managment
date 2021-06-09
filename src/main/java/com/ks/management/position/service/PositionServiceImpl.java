package com.ks.management.position.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ks.management.position.Position;
import com.ks.management.position.dao.JpaPositionRepo;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {
	@Autowired
	private JpaPositionRepo jpaPositionRepo;

	@Override
	public List<Position> getPositions() {
		return jpaPositionRepo.findAll().stream()
				.sorted(Comparator.comparing(Position::getCode))
				.collect(Collectors.toList());
	}

	@Override
	public Position getPositionByName(String name) {
		return jpaPositionRepo.findByName(name);
	}

}
