package com.ks.management.recruitment.training.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.TrainingConfirmationType;
import com.ks.management.recruitment.training.TrainingNote;
import com.ks.management.recruitment.training.dao.JpaTraining;
import com.ks.management.recruitment.training.dao.JpaTrainingConfirmationType;
import com.ks.management.recruitment.training.ui.TrainingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService{
    @Autowired
    private JpaTraining jpaTraining;
    @Autowired
    private JpaEmployeeRepo employeeRepo;
    @Autowired
    private JpaTrainingConfirmationType jpaTrainingConfirmationType;

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

    @Override
    public Training getTrainingById(int trainingId) {
        final Optional<Training> training = jpaTraining.findById(trainingId);
        if(!training.isPresent()){
            throw new RuntimeException("Cant find training with ID:" + trainingId);
        }
        return training.get();
    }

    @Override
    public TrainingDto getTrainingDtoById(int trainingId) {
        final Optional<Training> training = jpaTraining.findById(trainingId);
        if(!training.isPresent()){
            throw new RuntimeException("Cant find training with ID:" + trainingId);
        }
        final Set<Employee> trainerOptions = employeeRepo.findAllNonCanvassers();
        final Set<TrainingConfirmationType> confirmationTypes = new HashSet<>(jpaTrainingConfirmationType.findAll());
        final TrainingDto trainingDto = TrainingDto.builder()
                .id(training.map(t -> t.getId()).orElse(-1))
                .application(training.map(t -> t.getApplication()).orElse(null))
                .interview(training.map(t -> t.getInterview()).orElse(null))
                .scheduledTime(training.map(t -> t.getScheduledTime()).orElse(null))
                .trainer(training.map(t -> t.getTrainer()).orElse(null))
                .trainingConfirmationType(training.map(t -> t.getTrainingConfirmationType()).orElse(null))
                .hasShow(training.map(t -> t.getHasShow()).orElse(null))
                .updatedBy(training.map(t -> t.getUpdatedBy()).orElse(null))
                .updatedDate(training.map(t -> t.getUpdatedDate()).orElse(null))
                .createdBy(training.map(t -> t.getCreatedBy()).orElse(null))
                .createdDate(training.map(t -> t.getCreatedDate()).orElse(null))
                .trainingNotes(training.map(t -> t.getTrainingNotes()).orElse(Collections.emptyList()))
                .trainerOptions(trainerOptions)
                .trainingConfirmationTypeOptions(confirmationTypes)
                .build();

        return trainingDto;
    }

    @Override
    public Training updateTraining(Training training) {
        //TODO: set by auth user
        training.setUpdatedBy(-1);
        if(training.getTrainingNotes() != null){
            final TrainingNote note = training.getTrainingNotes().get(0);
            //TODO: auth user
            note.setCreatedBy(-1);
            note.setUpdatedBy(-1);
            note.setTraining(training);
            training.addTrainingNote(note);
        }
        return jpaTraining.save(training);
    }
}
