package com.ks.management.recruitment.training.controller;

import com.ks.management.recruitment.training.Training;
import com.ks.management.recruitment.training.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
