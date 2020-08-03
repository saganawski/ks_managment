package com.ks.management.recruitment.training.service;

import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.ui.TrainingDto;
import com.ks.management.security.UserPrincipal;

import java.util.List;

public interface TrainingService {
    List<Training> getAllTrainings();

    Training createTraining(Training training, UserPrincipal userPrincipal);

    Training getTrainingById(int trainingId);

    TrainingDto getTrainingDtoById(int trainingId);

    Training updateTraining(Training training, UserPrincipal userPrincipal);

    void deleteNote(int noteId);

    void deleteTraining(int trainingId);
}
