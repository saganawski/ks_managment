package com.ks.management.recruitment.interview.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InterviewDtoByOffice {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Date scheduledTime;
    private String interviewers;
    private String officeName;
}
