package com.ks.management.recruitment.interview.dao;

import com.ks.management.recruitment.interview.InterviewResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInterviewResult extends JpaRepository<InterviewResult,Integer> {
}
