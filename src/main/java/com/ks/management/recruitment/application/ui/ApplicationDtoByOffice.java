package com.ks.management.recruitment.application.ui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApplicationDtoByOffice {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Date dateReceived;
    private Date callBackDate;
    private String source;
    private String result;
    private String name;
    private Date createdDate;
}
