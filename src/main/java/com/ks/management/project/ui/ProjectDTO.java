package com.ks.management.project.ui;

import com.ks.management.office.Office;
import com.ks.management.project.ProjectWeek;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectDTO {
    private Integer id;
    private String name;
    private Integer shiftGoal;
    private Integer shiftsScheduled;
    private Integer shiftCompleted;
    private Integer remainingWorkingDays;
    private Integer shiftsNeeded;
    private Office office;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;
    private Set<ProjectWeek> projectWeeks = new HashSet<>();
}
