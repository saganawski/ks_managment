package com.ks.management.recruitment.interview.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.employee.Employee;
import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.interview.InterviewConfirmationType;
import com.ks.management.recruitment.interview.InterviewNote;
import com.ks.management.recruitment.interview.InterviewResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InterviewDto {

    private Integer id;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Application application;
    private Date scheduledTime;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee scheduler;
    private InterviewConfirmationType interviewConfirmationType;
    private InterviewResult interviewResult;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;
    private List<Integer> interviewersId = new ArrayList<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<InterviewNote> interviewNotes = new ArrayList<>();

    public void addInterviewNote(InterviewNote note){
        if(interviewNotes == null){
            interviewNotes = new ArrayList<>();
        }
        interviewNotes.add(note);
    }

}
