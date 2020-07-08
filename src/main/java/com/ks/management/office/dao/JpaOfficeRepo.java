package com.ks.management.office.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ks.management.office.Office;

public interface JpaOfficeRepo extends JpaRepository<Office, Integer> {

}
