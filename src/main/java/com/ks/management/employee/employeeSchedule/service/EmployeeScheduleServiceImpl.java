package com.ks.management.employee.employeeSchedule.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.employee.employeeSchedule.EmployeeScheduleStatus;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleRepo;
import com.ks.management.employee.employeeSchedule.dao.JpaEmployeeScheduleStatusRepo;
import com.ks.management.employee.employeeSchedulePayRoll.EmployeeSchedulePayroll;
import com.ks.management.employee.employeeSchedulePayRoll.dao.JpaEmployeeSchedulePayroll;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
    @Autowired
    JpaEmployeeSchedulePayroll jpaEmployeeSchedulePayroll;

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

    @Override
    public EmployeeSchedule setEmployeeScheduleStatusAndPayRoll(Integer employeeScheduleId, EmployeeSchedule givenEmployeeSchedule, UserPrincipal userPrincipal) {

        final Integer userId = userPrincipal.getUserId();

        final EmployeeSchedule employeeSchedule = jpaEmployeeScheduleRepo.getOne(employeeScheduleId);
        if(employeeSchedule == null){
            throw new RuntimeException("Could not find Employee Schedule in Database! \n contact tech support!");
        }
        employeeSchedule.setUpdatedBy(userId);

        final String statusCode = Optional.ofNullable(givenEmployeeSchedule.getEmployeeScheduleStatus()).map(s -> s.getCode()).orElse(null);
        if(statusCode == null){
            throw new RuntimeException("Could not find status in database! \n contact tech support!");
        }
        final EmployeeScheduleStatus employeeScheduleStatus = jpaEmployeeScheduleStatusRepo.findByCode(statusCode);
        employeeSchedule.setEmployeeScheduleStatus(employeeScheduleStatus);

        if(givenEmployeeSchedule.getEmployeeSchedulePayroll() != null){
            final Integer employeeSchedulePayrollId = givenEmployeeSchedule.getEmployeeSchedulePayroll().getId();
            EmployeeSchedulePayroll employeeSchedulePayRoll = null;
            if(employeeSchedulePayrollId != null){

                employeeSchedulePayRoll = jpaEmployeeSchedulePayroll.getOne(employeeSchedulePayrollId);

                setPayrollValues(givenEmployeeSchedule, userId, employeeSchedulePayRoll);
            }else {
                employeeSchedulePayRoll = givenEmployeeSchedule.getEmployeeSchedulePayroll();
                setPayrollValues(givenEmployeeSchedule, userId, employeeSchedulePayRoll);
                employeeSchedulePayRoll.setCreatedBy(userId);
            }

            jpaEmployeeSchedulePayroll.save(employeeSchedulePayRoll);
            employeeSchedule.setEmployeeSchedulePayroll(employeeSchedulePayRoll);
        }

        return jpaEmployeeScheduleRepo.save(employeeSchedule);
    }

    private void setPayrollValues(EmployeeSchedule givenEmployeeSchedule, Integer userId, EmployeeSchedulePayroll employeeSchedulePayRoll) {
        final LocalTime timeIn = givenEmployeeSchedule.getEmployeeSchedulePayroll().getTimeIn();
        final LocalTime timeOut = givenEmployeeSchedule.getEmployeeSchedulePayroll().getTimeOut();
        final Integer mileage = givenEmployeeSchedule.getEmployeeSchedulePayroll().getMileage();
        final Double payRate = givenEmployeeSchedule.getEmployeeSchedulePayroll().getPayRate();

        long timeWorked = timeIn.until(timeOut, ChronoUnit.MINUTES);

        if(timeWorked >=  300){
            employeeSchedulePayRoll.setLunch(true);
            timeWorked = timeWorked - 30;
        }

        final Double hoursWorked = Double.valueOf( (double) timeWorked / 60);
        double totalDayWages = hoursWorked * payRate;
        if(mileage != null){
            double mileageBonus = (double) mileage * .51;
            totalDayWages = totalDayWages + mileageBonus;
        }

        employeeSchedulePayRoll.setTotalHours((double) timeWorked);
        employeeSchedulePayRoll.setTimeIn(timeIn);
        employeeSchedulePayRoll.setTimeOut(timeOut);
        employeeSchedulePayRoll.setMileage(mileage);
        employeeSchedulePayRoll.setPayRate(payRate);
        employeeSchedulePayRoll.setTotalDayWage(totalDayWages);
        employeeSchedulePayRoll.setUpdatedBy(userId);
    }
}
