package com.ks.management.employee.employeeSchedule.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.EmployeeScheduleStatus;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleRepo;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleStatusRepo;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeScheduleServiceImpl implements EmployeeScheduleService{
    @Autowired
    JpaEmployeeRepo jpaEmployeeRepo;
    @Autowired
    JpaEmployeeScheduleRepo jpaEmployeeScheduleRepo;
    @Autowired
    JpaEmployeeScheduleStatusRepo jpaEmployeeScheduleStatusRepo;

    @Override
    public Employee createScheduleEmployee(Integer employeeId, List<LocalDateTime> scheduleDates, UserPrincipal userPrincipal) {
        final Integer userId = userPrincipal.getUserId();

        final Employee employee = jpaEmployeeRepo.getOne(employeeId);
        if(employee == null){
            throw new RuntimeException("Employee not found");
        }
        // get all current schedule dates
        final List<LocalDateTime> currentSchedules = jpaEmployeeScheduleRepo.findAllByEmployeeId(employeeId).stream()
                .map(es -> es.getScheduledTime()).collect(Collectors.toList());

        scheduleDates.removeAll(currentSchedules);
        scheduleDates.forEach(sd -> {
            final EmployeeSchedule employeeSchedule =  EmployeeSchedule.builder()
                    .employee(employee)
                    .scheduledTime(sd)
                    .updatedBy(userId)
                    .createdBy(userId)
                    .build();
            jpaEmployeeScheduleRepo.save(employeeSchedule);
        });

        return employee;
    }

    @Override
    public List<EmployeeSchedule> getSchedules(Integer employeeId) {
        return jpaEmployeeScheduleRepo.findAllByEmployeeId(employeeId);
    }

    @Override
    public void deleteEmployeeScheduleById(Integer employeeScheduleId) {
        jpaEmployeeScheduleRepo.deleteById(employeeScheduleId);
    }

    @Override
    public List<EmployeeSchedule> getEmployeeSchedulesByOffice(Integer officeId) {
        return jpaEmployeeScheduleRepo.findAllByOffice(officeId);
    }

    @Override
    public EmployeeSchedule setEmployeeScheduleStatus(Integer employeeScheduleId, EmployeeScheduleStatus status, UserPrincipal userPrincipal) {
        final EmployeeScheduleStatus employeeScheduleStatus = jpaEmployeeScheduleStatusRepo.findByCode(status.getCode());

        if(employeeScheduleStatus == null){
            throw new RuntimeException("Could not find status in database! \n contact tech support!");
        }
        final EmployeeSchedule employeeSchedule = jpaEmployeeScheduleRepo.getOne(employeeScheduleId);

        employeeSchedule.setUpdatedBy(userPrincipal.getUserId());
        employeeSchedule.setEmployeeScheduleStatus(employeeScheduleStatus);
        jpaEmployeeScheduleRepo.save(employeeSchedule);
        return  employeeSchedule;
    }

    @Override
    public List<EmployeeSchedule> getEmployeeSchedulesByOfficeForTimePeriod(Integer officeId, String startDate, String endDate) {
        final Instant startInstance = Instant.parse(startDate);
        final Instant endInstance = Instant.parse(endDate);
        final LocalDateTime startDateParam = LocalDateTime.ofInstant(startInstance, ZoneId.of(ZoneOffset.UTC.getId()));
        final LocalDateTime endDateParam = LocalDateTime.ofInstant(endInstance,ZoneId.of(ZoneOffset.UTC.getId()));

        return jpaEmployeeScheduleRepo.findAllByOfficeForTimePeriod(officeId,startDateParam.with(LocalTime.MIN),endDateParam);
    }
}
