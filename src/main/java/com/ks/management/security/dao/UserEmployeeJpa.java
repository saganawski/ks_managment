package com.ks.management.security.dao;

import com.ks.management.security.UserEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEmployeeJpa extends JpaRepository<UserEmployee, Integer> {
}
