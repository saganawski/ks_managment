package com.ks.management.location.dao;

import com.ks.management.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLocationDao extends JpaRepository<Location,Integer> {
}
