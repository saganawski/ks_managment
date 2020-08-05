package com.ks.management.employee.dao;

import com.ks.management.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface JpaEmployeeRepo extends JpaRepository<Employee, Integer> {
    @Query(value = "Select e.* FROM employee as e " +
            "JOIN position as p ON p.id = e.position_id " +
            "WHERE p.code != 'CANVASSER'",
            nativeQuery = true)
    Set<Employee> findAllNonCanvassers();
}
