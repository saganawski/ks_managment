package com.ks.management.recruitment.training.service;

import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.ui.TrainingDto;

import java.util.List;

public interface TrainingService {
    List<Training> getAllTrainings();

    Training createTraining(Training training);

    Training getTrainingById(int trainingId);

    TrainingDto getTrainingDtoById(int trainingId);

    Training updateTraining(Training training);

    void deleteNote(int noteId);
}
