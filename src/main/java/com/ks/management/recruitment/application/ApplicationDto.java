package com.ks.management.recruitment.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ks.management.office.Office;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicationDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Date dateReceived;
    private Date callBackDate;
    private Integer updatedBy;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ApplicationContactType applicationContactType;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ApplicationSource applicationSource;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ApplicationResult applicationResult;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Office office;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ApplicationNote> applicationNotes;
    private Date updatedDate;
    private Integer createdBy;
    private Date createdDate;

}
