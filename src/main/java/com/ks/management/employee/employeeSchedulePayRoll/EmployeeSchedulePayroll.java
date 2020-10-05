package com.ks.management.employee.employeeSchedulePayRoll;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name="employee_schedule_payroll")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeSchedulePayroll {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "pay_rate")
    private Double payRate;

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
    private LocalTime timeOut;

    @Column(name = "lunch")
    private Boolean lunch;

    @Column(name = "overtime")
    private Boolean overtime;

    @Column(name = "total_minutes")
    private Double totalMinutes;

    @Column(name = "overtime_minutes")
    private Double overtimeMinutes;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "total_day_wage")
    private Double totalDayWage;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

}
