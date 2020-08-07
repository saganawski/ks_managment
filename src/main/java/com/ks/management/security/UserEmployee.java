package com.ks.management.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ks.management.employee.Employee;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user_employee")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEmployee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    private Employee employee;

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
