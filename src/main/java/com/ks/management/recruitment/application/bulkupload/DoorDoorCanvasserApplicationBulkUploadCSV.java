package com.ks.management.recruitment.application.bulkupload;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class DoorDoorCanvasserApplicationBulkUploadCSV {
    private final static Integer NAME = 0;
    private final static Integer EMAIL = 1;
    private final static Integer PHONE = 2;
    private final static Integer CANDIDATE_LOCATION = 4;
    private final static Integer CURRENT_ROLE = 5;
    private final static Integer EDUCATION = 6;
    private final static Integer JOB_TITLE = 7;
    private final static Integer JOB_LOCATION = 8;
    private final static Integer DATE = 9;
    private final static Integer INTEREST_LEVEL = 10;
    private final static Integer SOURCE = 11;

    private String[] application = null;
    private String name;
    private String email;
    private String phone;
    private String candidateLocation;
    private String currentRole;
    private String education;
    private String jobTitle;
    private String jobLocation;
    private Date date;
    private String interestLevel;
    private String source;

    public DoorDoorCanvasserApplicationBulkUploadCSV(List<String> application) {
        try{
            this.name = application.get(NAME);
            this.email = application.get(EMAIL);
            this.phone =application.get(PHONE);
            this.candidateLocation = application.get(CANDIDATE_LOCATION);
            this.currentRole = application.get(CURRENT_ROLE);
            this.education = application.get(EDUCATION);
            this.jobTitle = application.get(JOB_TITLE);
            this.jobLocation = application.get(JOB_LOCATION);

            Date parsedDate;
            try{
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                String dateString = application.get(DATE);
                parsedDate = formatter.parse(dateString);

            }catch (ParseException pe){
                pe.printStackTrace();
                throw new RuntimeException("couldn't parse date" + pe.getMessage());
            }

            this.date = parsedDate;
            this.interestLevel = application.get(INTEREST_LEVEL);
            this.source = application.get(SOURCE);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Unable to make Application \n" + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ApplicationBulkUpload{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", candidateLocation='" + candidateLocation + '\'' +
                ", currentRole='" + currentRole + '\'' +
                ", education='" + education + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", jobLocation='" + jobLocation + '\'' +
                ", date=" + date +
                ", interestLevel='" + interestLevel + '\'' +
                ", source='" + source + '\'' +
                '}';
    }

    public static List<String> cleanInput(String application){
        if(application.length() < 1){
            return Collections.emptyList();
        }

        final List<String> cleanedInput = Stream.of(application.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
                .map(String::trim)
                .collect(Collectors.toList());

        return cleanedInput;
    }
}
