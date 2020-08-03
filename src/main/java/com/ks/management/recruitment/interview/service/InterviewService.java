package com.ks.management.recruitment.interview.service;

import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.ui.InterviewApplicationDto;
import com.ks.management.recruitment.interview.ui.InterviewDto;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface InterviewService {
    List<Interview> getAllInterviews();

    Interview createInterview(Interview interview, UserPrincipal userPrincipal);

    InterviewApplicationDto findInterviewApplicationDto(int interviewId);

    Interview updateInterview(InterviewDto interviewDto, UserPrincipal userPrincipal);

    void deleteNote(int noteId);

    void deleteInterview(int interviewId);
}
