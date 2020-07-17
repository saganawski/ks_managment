package com.ks.management.recruitment.interview.dao;

import com.ks.management.recruitment.interview.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInterview extends JpaRepository<Interview,Integer> {
}
