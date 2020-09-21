package com.ks.management.project.dao;

import com.ks.management.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProjectRepo extends JpaRepository<Project, Integer> {
}
