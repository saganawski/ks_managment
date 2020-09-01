package com.ks.management.employee.employeeSchedule.dao;

import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaEmployeeScheduleRepo extends JpaRepository<EmployeeSchedule, Integer> {
    @Query(value = "Select * FROM employee_schedule " +
            "WHERE employee_id = ?1 ",
            nativeQuery = true)
    List<EmployeeSchedule> findAllByEmployeeId(Integer employeeId);
}
