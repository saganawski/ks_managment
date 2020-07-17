package com.ks.management.recruitment.interview.controller;

import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/interviews")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;

    @GetMapping()
    public List<Interview> getInterviews(){
        return interviewService.getAllInterviews();
    }
}
