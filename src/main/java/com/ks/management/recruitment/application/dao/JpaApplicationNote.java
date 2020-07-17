package com.ks.management.recruitment.application.dao;

import com.ks.management.recruitment.application.ApplicationNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaApplicationNote extends JpaRepository<ApplicationNote,Integer> {
}
