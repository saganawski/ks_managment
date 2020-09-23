package com.ks.management.employee.employeeSchedulePayRoll;

import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="employee_schedule_payroll")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeSchedulePayRoll {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_schedule_id", nullable = false)
    private EmployeeSchedule employeeSchedule;

    @Column(name = "pay_rate")
    private Double payRate;

    @Column(name = "time_in")
    private LocalDateTime timeIn;

    @Column(name = "time_out")
    private LocalDateTime timeOut;

    @Column(name = "lunch")
    private Boolean lunch;

    @Column(name = "overtime")
    private Boolean overtime;

    @Column(name = "total_hours")
    private Double totalHours;

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
