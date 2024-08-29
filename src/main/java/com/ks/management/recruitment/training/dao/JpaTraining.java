package com.ks.management.recruitment.training.dao;

import com.ks.management.recruitment.training.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaTraining extends JpaRepository<Training, Integer> {
    @Query(value = "Select * FROM training " +
            "WHERE application_id = ?1 AND interview_id = ?2 ",
            nativeQuery = true)
    List<Training> findAllByApplicationIdAndInterviewId(Integer applicationId, Integer interviewId);

    @Query(value = "Select * FROM training " +
            "WHERE employee_id = ?1 AND DATE(scheduled_time) = curdate() ",
            nativeQuery = true)
    List<Training> getTodaysTrainigs(Integer employeeId);

    @Query(value = "SELECT t.id, a.first_name, a.last_name, a.phone_number, t.scheduled_time, e.first_name AS trainerFirstName, e.last_name AS trainerLastName, o.name FROM training AS t " +
            "JOIN application AS a ON a.id = t.application_id " +
            "LEFT JOIN employee AS e on e.id = t.employee_id " +
            "JOIN office AS o on o.id = a.office_id " +
            "where o.id = ?1", nativeQuery = true)
    List<Object[]> getAllTrainingsByOfficeId(Integer officeId);

}
