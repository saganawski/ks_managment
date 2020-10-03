package com.ks.management.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.employee.employeeSchedule.EmployeeSchedule;
import com.ks.management.office.Office;
import com.ks.management.report.ui.PayrollDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="schedule_audit")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ScheduleAudit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "office_id")
    private Office office;

    @Column(name = "is_payroll")
    private Boolean isPayroll;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name= "schedule_audit_employee_schedule",
            joinColumns = @JoinColumn(name="schedule_audit_id"),
            inverseJoinColumns = @JoinColumn(name="employee_schedule_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<EmployeeSchedule> employeeSchedules = new ArrayList<>();

    public void addEmployeeSchedule(EmployeeSchedule employeeSchedule){
        if(employeeSchedules == null){
            employeeSchedules = new ArrayList<>();
        }
        employeeSchedules.add(employeeSchedule);
    }

}
