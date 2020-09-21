package com.ks.management.project.ui;

import com.ks.management.office.Office;
import com.ks.management.project.ProjectWeek;
import lombok.*;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectDTO {
    private Integer id;
    private String name;
    private Integer originalShiftGoal;
    private Integer currentShiftGoal;
    private Integer shiftsScheduled;
    private Integer shiftsCompleted;
    private Integer remainingWorkingDays;
    private Double shiftsNeeded;
    private Office office;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;
    private Set<ProjectWeek> projectWeeks = new HashSet<>();

    private Integer scheduledVsGoal;
    private Integer shiftsVsGoal;


    //totals needed
    public void setWeekTotals(){
        this.projectWeeks.forEach(pw ->{
            originalShiftGoal = originalShiftGoal + Optional.ofNullable(pw.getOriginalShiftGoal()).orElse(0);
            currentShiftGoal = currentShiftGoal + Optional.ofNullable(pw.getCurrentShiftGoal()).orElse(0);
            shiftsScheduled = shiftsScheduled + Optional.ofNullable(pw.getShiftsScheduled()).orElse(0);
            scheduledVsGoal = scheduledVsGoal + (Optional.ofNullable(pw.getShiftsScheduled()).orElse(0) - Optional.ofNullable(pw.getCurrentShiftGoal()).orElse(0));
            shiftsCompleted = shiftsCompleted + Optional.ofNullable(pw.getShiftsCompleted()).orElse(0);
            shiftsVsGoal = shiftsVsGoal + ( Optional.ofNullable(pw.getShiftsCompleted()).orElse(0) - Optional.ofNullable(pw.getCurrentShiftGoal()).orElse(0));
            remainingWorkingDays = remainingWorkingDays + Optional.ofNullable(pw.getRemainingWorkingDays()).orElse(0);
            shiftsNeeded = shiftsNeeded + Optional.ofNullable(pw.getShiftsNeeded()).orElse(0.0);
        });
    }
}
