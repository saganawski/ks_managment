package com.ks.management.recruitment.interview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ks.management.recruitment.application.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="interview_note")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewNote {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="interview_id", nullable = false)
    @JsonIgnore
    private Interview interview;

    @Column(name="note")
    private String note;

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
}
