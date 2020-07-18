package com.ks.management.recruitment.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.office.Office;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="application")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="email")
    private String email;

    @Column(name="date_received")
    private Date dateReceived;

    @Column(name="call_back_date")
    private Date callBackDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "office_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Office office;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name="application_contact_type_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ApplicationContactType applicationContactType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name="application_source_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ApplicationSource applicationSource;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name="application_result_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ApplicationResult applicationResult;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy = "application",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ApplicationNote> applicationNotes = new ArrayList<>();

    @Column(name = "updated_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public void addNote(ApplicationNote note){
        if(applicationNotes == null){
            applicationNotes = new ArrayList<>();
        }
        applicationNotes.add(note);
    }

    public void removeNote(ApplicationNote note){
        applicationNotes.remove(note);
    }

 /*   @OneToOne(mappedBy = "application")
    private Interview interview;*/

}
