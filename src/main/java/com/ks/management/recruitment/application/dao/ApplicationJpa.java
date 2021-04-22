package com.ks.management.recruitment.application.dao;

import com.ks.management.recruitment.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ApplicationJpa extends JpaRepository<Application,Integer> {

    @Query(value = "SELECT count(*) FROM application " +
            "WHERE office_id = ?1 AND created_date >= ?2 AND created_date <= ?3", nativeQuery = true)
    Integer getCountOfApplicationsForOfficeBetweenDates(Integer officeId, String startDateParam, String endDateParam);
}
