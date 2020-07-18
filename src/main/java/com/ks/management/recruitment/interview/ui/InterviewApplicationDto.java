package com.ks.management.recruitment.interview.ui;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.employee.Employee;
import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.application.ui.ApplicationFormOptionsDto;
import com.ks.management.recruitment.interview.InterviewConfirmationType;
import com.ks.management.recruitment.interview.InterviewNote;
import com.ks.management.recruitment.interview.InterviewResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InterviewApplicationDto {
    private Integer interviewId;
    private Application application;
    private Date scheduledTime;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee scheduler;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private InterviewConfirmationType interviewConfirmationType;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private InterviewResult interviewResult;
    private Integer updatedBy;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Employee> interviewers = new ArrayList<>();

    public void addInterviewer(Employee interviewer){
        if(interviewers == null){
            interviewers = new ArrayList<>();
        }
        interviewers.add(interviewer);
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<InterviewNote> interviewNotes = new ArrayList<>();

//    Options for the forms
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Employee> schedulersOptions = new HashSet<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Employee> interviewersOptions = new HashSet<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<InterviewConfirmationType> confirmationTypesOptions = new HashSet<>();
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<InterviewResult> interviewResultsOptions = new HashSet<>();
    private ApplicationFormOptionsDto applicationFormOptionsDto;



}
