package com.ks.management.recruitment.training.dao;

import com.ks.management.recruitment.training.TrainingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTrainingNote extends JpaRepository<TrainingNote,Integer> {
}
