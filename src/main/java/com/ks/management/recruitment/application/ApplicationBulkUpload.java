package com.ks.management.recruitment.application;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
public class ApplicationBulkUpload {
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
    private final static Integer QUALIFICATION_1 = 12;
    private final static Integer QUALIFICATION_1_ANWSER = 13;
    private final static Integer QUALIFICATION_1_MATCH = 14;
    private final static Integer QUALIFICATION_2 = 15;
    private final static Integer QUALIFICATION_2_ANWSER = 16;
    private final static Integer QUALIFICATION_2_MATCH = 17;

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
    private String qualification1;
    private String qualification1Answer;
    private String qualification1Match;
    private String qualification2;
    private String qualification2Answer;
    private String qualification2Match;

    public ApplicationBulkUpload(List<String> application) {
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
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = application.get(DATE);
            parsedDate = formatter.parse(dateString);

        }catch (ParseException pe){
            pe.printStackTrace();
            throw new RuntimeException("couldnt parse" + pe.getMessage());
        }

        this.date = parsedDate;
        this.interestLevel = application.get(INTEREST_LEVEL);
        this.source = application.get(SOURCE);
        this.qualification1 = application.get(QUALIFICATION_1);
        this.qualification1Answer = application.get(QUALIFICATION_1_ANWSER);
        this.qualification1Match = application.get(QUALIFICATION_1_MATCH);
        this.qualification2 = application.get(QUALIFICATION_2);
        this.qualification2Answer = application.get(QUALIFICATION_2_ANWSER);
        this.qualification2Match = application.get(QUALIFICATION_2_MATCH);
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
                ", qualification1='" + qualification1 + '\'' +
                ", qualification1Answer='" + qualification1Answer + '\'' +
                ", qualification1Match='" + qualification1Match + '\'' +
                ", qualification2='" + qualification2 + '\'' +
                ", qualification2Answer='" + qualification2Answer + '\'' +
                ", qualification2Match='" + qualification2Match + '\'' +
                '}';
    }

    public static List<String> cleanInput(String application){
        if(application.length() < 1){
            return Collections.emptyList();
        }
        final List<String> cleanedInput = new ArrayList<>();
        String[] values = application.split("\\t");
        for(String val: values){
            final String removeNonPrintChars = val.replaceAll("\\P{Print}", "");
            final String removeQuotes = removeNonPrintChars.replaceAll("\"","");
            cleanedInput.add(removeQuotes);
        }
        return cleanedInput;
    }
}
