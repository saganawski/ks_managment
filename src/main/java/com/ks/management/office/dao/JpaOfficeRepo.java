package com.ks.management.office.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ks.management.office.Office;

import java.util.Optional;

public interface JpaOfficeRepo extends JpaRepository<Office, Integer> {

    Optional<Office> findByName(String sourceJobLocation);
}
