package com.ks.management.recruitment.training.ui;

import com.ks.management.employee.Employee;
import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.training.TrainingConfirmationType;
import com.ks.management.recruitment.training.TrainingNote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TrainingDto {
    private Integer id;
    private Application application;
    private Interview interview;
    private Date scheduledTime;
    private Employee trainer;
    private TrainingConfirmationType trainingConfirmationType;
    private Boolean hasShow;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;
    private List<TrainingNote> trainingNotes = new ArrayList<>();

    private Set<Employee> trainerOptions = new HashSet<>();
    private Set<TrainingConfirmationType> trainingConfirmationTypeOptions = new HashSet<>();

}
