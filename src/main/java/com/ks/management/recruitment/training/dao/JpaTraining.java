package com.ks.management.recruitment.training.dao;

import com.ks.management.employee.Employee;
import com.ks.management.recruitment.training.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface JpaTraining extends JpaRepository<Training, Integer> {
    @Query(value = "Select * FROM training " +
            "WHERE application_id = ?1 AND interview_id = ?2 ",
            nativeQuery = true)
    List<Training> findAllByApplicationIdAndInterviewId(Integer applicationId, Integer interviewId);

    @Query(value = "Select * FROM training " +
            "WHERE employee_id = ?1 AND DATE(scheduled_time) = curdate() ",
            nativeQuery = true)
    List<Training> getTodaysTrainigs(Integer employeeId);
}
