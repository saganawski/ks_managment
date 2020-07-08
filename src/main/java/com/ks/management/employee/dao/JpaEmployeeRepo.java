package com.ks.management.employee.dao;

import com.ks.management.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaEmployeeRepo extends JpaRepository<Employee, Integer> {
}
