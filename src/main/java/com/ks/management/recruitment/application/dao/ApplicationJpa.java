package com.ks.management.recruitment.application.dao;

import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.application.ui.ApplicationDtoByOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationJpa extends JpaRepository<Application,Integer> {

    @Query(value = "SELECT count(*) FROM application " +
            "WHERE office_id = ?1 AND created_date >= ?2 AND created_date <= ?3", nativeQuery = true)
    Integer getCountOfApplicationsForOfficeBetweenDates(Integer officeId, String startDateParam, String endDateParam);
    @Query(value = "Select ap.id, ap.first_name, ap.last_name, ap.phone_number, ap.email, ap.date_received, ap.call_back_date, ap.created_date, s.source, o.name FROM application AS ap " +
            "JOIN application_source AS s ON s.id = ap.application_source_id " +
            "JOIN office AS o ON o.id = ap.office_id " +
            "WHERE office_id = ?1", nativeQuery = true)
    List<Object[]> getAllApplicationsByOfficeId(Integer officeId);
}
