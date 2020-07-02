package com.ks.management.position.service;

import java.util.List;

import com.ks.management.position.Position;

public interface PositionService {

	List<Position> getPositions();

	Position getPositionByName(String name);

}
