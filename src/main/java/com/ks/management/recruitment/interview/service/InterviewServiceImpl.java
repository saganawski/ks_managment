package com.ks.management.recruitment.interview.service;

import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.dao.JpaInterview;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private JpaInterview jpaInterview;

    @Override
    public List<Interview> getAllInterviews() {
        return jpaInterview.findAll();
    }
}
