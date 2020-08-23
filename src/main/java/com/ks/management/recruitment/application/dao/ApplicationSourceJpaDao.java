package com.ks.management.recruitment.application.dao;

import com.ks.management.recruitment.application.ApplicationSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationSourceJpaDao extends JpaRepository<ApplicationSource,Integer> {
    ApplicationSource findByCode(String indeed);
}
