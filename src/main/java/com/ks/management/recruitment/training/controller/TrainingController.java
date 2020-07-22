package com.ks.management.recruitment.training.controller;

import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.service.TrainingService;
import com.ks.management.recruitment.training.ui.TrainingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    @Autowired
    private TrainingService trainingService;

    @GetMapping()
    public List<Training> getTrainings(){
        return trainingService.getAllTrainings();
    }

    @PostMapping()
    public Training createTraining(@RequestBody Training training){
        return trainingService.createTraining(training);
    }

    @GetMapping("/{trainingId}")
    public Training getTraining(@PathVariable("trainingId") int trainingId){
        return trainingService.getTrainingById(trainingId);
    }

    @GetMapping("/{trainingId}/dto")
    public TrainingDto getTrainingDto(@PathVariable("trainingId") int trainingId){
        return trainingService.getTrainingDtoById(trainingId);
    }

    @PutMapping("/{trainingId}")
    public Training updateTraining(@PathVariable("trainingId") Integer trainingId, @RequestBody Training training){
        if(!trainingId.equals(training.getId())){
            throw new RuntimeException("Id in path does not match request body");
        }
        return trainingService.updateTraining(training);
    }

    @DeleteMapping("/{trainingId}/notes/{noteId}")
    public void deleteNotes(@PathVariable("trainingId") int trainingId, @PathVariable("noteId") int noteId){
        trainingService.deleteNote(noteId);
    }

}
