package com.ks.management.recruitment.training.controller;

import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.service.TrainingService;
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

    @GetMapping("{trainingId}")
    public Training getTraining(@PathVariable("trainingId") int trainingId){
        return trainingService.getTrainingById(trainingId);
    }

}
