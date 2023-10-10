package com.ks.management.recruitment.training.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TrainingDtoByOffice {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Date scheduledTime;
    private String trainer;
    private String officeName;
}
