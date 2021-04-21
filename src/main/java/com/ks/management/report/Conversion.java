package com.ks.management.report;

import com.ks.management.office.Office;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="conversion")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Conversion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "office_id")
    private Office office;

    @Column(name = "total_applications")
    private Integer totalApplications;

    @Column(name = "total_interviews_scheduled")
    private Integer totalInterviewsScheduled;

    @Column(name = "total_interviews_show")
    private Integer totalInterviewsShow;

    @Column(name = "total_hires")
    private Integer totalHires;

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