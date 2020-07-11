package com.ks.management.interview.dao;

import com.ks.management.interview.Interviewee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervieweeJpaDao extends JpaRepository<Interviewee,Integer> {
}
