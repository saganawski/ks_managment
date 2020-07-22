package com.ks.management.recruitment.training;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.employee.Employee;
import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.interview.Interview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="training")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Training {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToOne
    @JoinColumn(name="application_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Application application;

    @OneToOne
    @JoinColumn(name="interview_id", referencedColumnName = "id")
    private Interview interview;

    @Column(name = "scheduled_time")
    private Date scheduledTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employee trainer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "training_confirmation_type_id")
    private TrainingConfirmationType trainingConfirmationType;

    @Column(name = "has_show")
    private Boolean has_show;

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
    @OneToMany(mappedBy = "training",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<TrainingNote> trainingNotes = new ArrayList<>();

    public void addTrainingNote(TrainingNote note){
        if(trainingNotes == null){
            trainingNotes = new ArrayList<>();
        }
        trainingNotes.add(note);
    }
}
