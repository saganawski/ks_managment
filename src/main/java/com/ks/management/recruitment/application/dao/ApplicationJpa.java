package com.ks.management.recruitment.application.dao;

import com.ks.management.recruitment.application.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationJpa extends JpaRepository<Application,Integer> {
}
