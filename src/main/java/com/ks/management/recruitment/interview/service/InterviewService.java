package com.ks.management.recruitment.interview.service;

import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.ui.InterviewApplicationDto;
import com.ks.management.recruitment.interview.ui.InterviewDto;

import java.util.List;

public interface InterviewService {
    List<Interview> getAllInterviews();

    Interview createInterview(Interview interview);

    InterviewApplicationDto findInterviewApplicationDto(int interviewId);

    Interview updateInterview(InterviewDto interviewDto);
}
