package com.ks.management.recruitment.application;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="application_source")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationSource {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="source")
    private String source;

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
