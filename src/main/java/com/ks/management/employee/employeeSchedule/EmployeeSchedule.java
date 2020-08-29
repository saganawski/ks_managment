package com.ks.management.employee.employeeSchedule;

import com.ks.management.employee.Employee;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="employee_schedule")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeSchedule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable = false)
    private Employee employee;

    @Column(name = "scheduled_time")
    private LocalDate scheduledTime;

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

    @Override
    public String toString() {
        return "EmployeeSchedule{" +
                "id=" + id +
                ", employeeId=" + employee.getId() +
                ", scheduledTime=" + scheduledTime +
                ", updatedBy=" + updatedBy +
                ", updatedDate=" + updatedDate +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                '}';
    }

}
