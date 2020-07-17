package com.ks.management.recruitment.interview.controller;

import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    public Interview createInterview(@RequestBody Interview interview){
        System.out.println("interview = " + interview);
        return  interviewService.createInterview(interview);
    }
}
