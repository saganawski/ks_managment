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

    @Query(value = "SELECT count(*) FROM interview AS i " +
            "JOIN application AS a ON a.id  = i.application_id " +
            "WHERE a.office_id = ?1 AND i.created_date >= ?2 AND i.created_date <= ?3", nativeQuery = true)
    Integer getInterviewsCountForOfficeBetweenDates(Integer officeId, String startDate, String endDate);

    @Query(value = "SELECT count(*) FROM interview AS i " +
            "JOIN application AS a ON a.id  = i.application_id " +
            "JOIN interview_result AS ir ON ir.id = i.interview_result_id " +
            "WHERE a.office_id = ?1 AND ir.result != 'No Show' " + //TODO: update code column
            "AND i.created_date >= ?2 AND i.created_date <= ?3", nativeQuery = true)
    Integer getInterviewsShowCountForOfficeBetweenDates(Integer officeId, String startDate, String endDate);

    @Query(value = "SELECT count(*) FROM interview AS i " +
            "JOIN application AS a ON a.id  = i.application_id " +
            "JOIN interview_result AS ir ON ir.id = i.interview_result_id " +
            "WHERE a.office_id = ?1 AND ir.code = 'HIRED' " +
            "AND i.created_date >= ?2 AND i.created_date <= ?3", nativeQuery = true)
    Integer getInterviewsHireCountForOfficeBetweenDates(Integer officeId, String startDate, String endDate);

    @Query(value = "SELECT i.id, a.first_name, a.last_name, a.phone_number, a.email, i.scheduled_time, GROUP_CONCAT(e.last_name SEPARATOR ', ') as interviewers, o.name  FROM interview AS i " +
            " JOIN application AS a on a.id = i.application_id " +
            " JOIN office AS o ON o.id = a.office_id " +
            " JOIN interview_director AS id ON id.interview_id = i.id " +
            " JOIN employee AS e on e.id = id.employee_id " +
            " where o.id = ?1 group by i.id", nativeQuery = true)
    List<Object[]> getAllInterviewsByOfficeId(Integer officeId);
}