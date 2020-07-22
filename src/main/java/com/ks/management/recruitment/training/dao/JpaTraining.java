package com.ks.management.recruitment.training.dao;

import com.ks.management.recruitment.training.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTraining extends JpaRepository<Training, Integer> {
}
