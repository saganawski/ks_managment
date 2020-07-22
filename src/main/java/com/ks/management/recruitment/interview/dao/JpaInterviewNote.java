package com.ks.management.recruitment.interview.dao;

import com.ks.management.recruitment.interview.InterviewNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInterviewNote extends JpaRepository<InterviewNote, Integer> {
}
