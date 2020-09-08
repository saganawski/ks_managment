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

    @Query(value = "SELECT es.* FROM employee_schedule AS es " +
            "JOIN employee_office AS eo ON eo.employee_id = es.employee_id " +
            "JOIN office AS o ON o.id = eo.office_id " +
            "JOIN employee AS e ON e.id = es.employee_id " +
            "WHERE o.id = ?1 AND e.deleted IS FALSE",
            nativeQuery = true)
    List<EmployeeSchedule> findAllByOffice(Integer officeId);
}
