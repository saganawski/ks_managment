package com.ks.management.recruitment.interview;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.employee.Employee;
import com.ks.management.recruitment.application.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="interview")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToOne
    @JoinColumn(name="application_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Application application;

    @Column(name = "scheduled_time")
    private Date scheduledTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee scheduler;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "interview_confirmation_type_id")
    private InterviewConfirmationType interviewConfirmationType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "interview_result_id")
    private InterviewResult interviewResult;

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
            name= "interview_director",
            joinColumns = @JoinColumn(name="interview_id"),
            inverseJoinColumns = @JoinColumn(name="employee_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Employee> interviewers = new ArrayList<>();

    public void addInterviewer(Employee interviewer){
        if(interviewers == null){
            interviewers = new ArrayList<>();
        }
        interviewers.add(interviewer);
    }

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy = "interview",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<InterviewNote> interviewNotes = new ArrayList<>();

    public void addInterviewNote(InterviewNote note){
        if(interviewNotes == null){
            interviewNotes = new ArrayList<>();
        }
        interviewNotes.add(note);
    }

}
