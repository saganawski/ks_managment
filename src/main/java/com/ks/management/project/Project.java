package com.ks.management.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.office.Office;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="project")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name = "shift_goal")
    private Integer shiftGoal;

    @Column(name = "shifts_scheduled")
    private Integer shiftsScheduled;

    @Column(name = "shifts_completed")
    private Integer shiftCompleted;

    @Column(name = "remaining_working_days")
    private Integer remainingWorkingDays;

    @Column(name = "shifts_needed")
    private Integer shiftsNeeded;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "office_id")
    private Office office;

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

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy = "project",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<ProjectWeek> projectWeeks = new HashSet<>();


}
