package com.ks.management.recruitment.interview.dao;

import com.ks.management.recruitment.interview.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaInterview extends JpaRepository<Interview,Integer> {
    @Query(value = "Select * FROM interview " +
            "WHERE application_id = ?1 ",
            nativeQuery = true)
    List<Interview> findAllByApplicationId(Integer applicationId);

    @Query(value = "Select * FROM interview AS i " +
            "JOIN interview_director AS id ON id.interview_id = i.id "+
            "WHERE id.employee_id = ?1 AND DATE(i.scheduled_time) = CURDATE() ",
            nativeQuery = true)
    List<Interview> findTodaysInterviews(Integer employeeId);
}