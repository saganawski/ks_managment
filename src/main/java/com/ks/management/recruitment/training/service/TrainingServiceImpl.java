package com.ks.management.recruitment.training.service;

import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.dao.JpaTraining;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService{
    @Autowired
    private JpaTraining jpaTraining;

    @Override
    public List<Training> getAllTrainings() {
        return jpaTraining.findAll();
    }

    @Override
    public Training createTraining(Training training) {
        //TODO: get active user
        training.setUpdatedBy(-1);
        training.setCreatedBy(-1);
        return jpaTraining.save(training);
    }
}
