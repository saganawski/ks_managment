package com.ks.management.employee.employeeSchedule.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleRepo;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class EmployeeScheduleServiceImpl implements EmployeeScheduleService{
    @Autowired
    JpaEmployeeRepo jpaEmployeeRepo;
    @Autowired
    JpaEmployeeScheduleRepo jpaEmployeeScheduleRepo;

    @Override
    public Employee createScheduleEmployee(Integer employeeId, String scheduleDate, UserPrincipal userPrincipal) {
        final Integer userId = userPrincipal.getUserId();

        final Employee employee = jpaEmployeeRepo.getOne(employeeId);
        if(employee == null){
            throw new RuntimeException("Employee not found");
        }

        final LocalDate scheduledDate = LocalDate.parse(scheduleDate);

        final EmployeeSchedule employeeSchedule =  EmployeeSchedule.builder()
                .employee(employee)
                .scheduledTime(scheduledDate)
                .updatedBy(userId)
                .createdBy(userId)
                .build();

        jpaEmployeeScheduleRepo.save(employeeSchedule);

        return employee;
    }
}
