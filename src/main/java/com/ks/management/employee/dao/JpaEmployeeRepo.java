package com.ks.management.employee.dao;

import com.ks.management.employee.Employee;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface JpaEmployeeRepo extends JpaRepository<Employee, Integer> {
    @Query(value = "Select e.* FROM employee as e " +
            "JOIN position as p ON p.id = e.position_id " +
            "WHERE p.code != 'CANVASSER' AND e.deleted IS FALSE ",
            nativeQuery = true)
    Set<Employee> findAllNonCanvassers();

    @Query(value = "Select * FROM employee " +
            "WHERE last_name = ?1 AND email = ?2 ",
            nativeQuery = true)
    Employee findByLastNameAndEmail(String lastName, String email);

}
