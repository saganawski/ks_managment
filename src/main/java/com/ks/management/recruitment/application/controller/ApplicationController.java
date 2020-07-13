package com.ks.management.recruitment.application.controller;

import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.application.ApplicationDto;
import com.ks.management.recruitment.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @GetMapping()
    public List<ApplicationDto> getApplications(){
        return applicationService.findAll();
    }

    @PostMapping()
    public Application createApplication(@RequestBody Application application){
        return applicationService.createApplication(application);
    }
}
