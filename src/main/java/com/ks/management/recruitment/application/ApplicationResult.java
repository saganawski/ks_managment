package com.ks.management.recruitment.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="application_result")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApplicationResult {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="result")
    private String result;

    @Column(name="code")
    private String code;

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
