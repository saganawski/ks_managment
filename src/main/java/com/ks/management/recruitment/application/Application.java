package com.ks.management.recruitment.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "updated_by")
    private Integer updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="application_contact_type_id")
    private ApplicationContactType applicationContactType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="application_source_id")
    private ApplicationSource applicationSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="application_result_id")
    private ApplicationResult applicationResult;

    @Column(name = "updated_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

}
