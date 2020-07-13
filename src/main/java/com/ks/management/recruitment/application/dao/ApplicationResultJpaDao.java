package com.ks.management.recruitment.application.dao;

import com.ks.management.recruitment.application.ApplicationResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationResultJpaDao extends JpaRepository<ApplicationResult,Integer> {
}
