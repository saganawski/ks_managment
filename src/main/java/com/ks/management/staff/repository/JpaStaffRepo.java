package com.ks.management.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ks.management.staff.Staff;

public interface JpaStaffRepo extends JpaRepository<Staff, Integer> {

}
