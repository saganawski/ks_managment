package com.ks.management.recruitment.application.controller;

import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.application.ApplicationDto;
import com.ks.management.recruitment.application.service.ApplicationService;
import com.ks.management.security.UserPrincipal;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Application createApplication(@RequestBody Application application, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return applicationService.createApplication(application, userPrincipal);
    }
    @GetMapping("/{applicationId}")
    public ApplicationDto getApplication(@PathVariable("applicationId") Integer applicationId){
        return applicationService.findBydId(applicationId);
    }

    @PutMapping("/{applicationId}")
    public Application updateApplication(@PathVariable("applicationId") Integer applicationId, @RequestBody Application application, @AuthenticationPrincipal UserPrincipal userPrincipal){
        if(!applicationId.equals(application.getId())){
            throw new RuntimeException("Id in path does not match request body");
        }
        return  applicationService.updateApplication(application,userPrincipal);
    }

    @DeleteMapping("/{applicationId}")
    public void deleteApplicationById(@PathVariable("applicationId") Integer applicationId){
        applicationService.deleteApplicationById(applicationId);
    }

    @DeleteMapping("/{applicationId}/notes/{noteId}")
    public void deleteNoteForApp(@PathVariable("applicationId") int applicationId, @PathVariable("noteId") int noteId){
        applicationService.deleteNoteForAppId(applicationId,noteId) ;
    }

    @PostMapping("/bulk-upload/bulk-type/{type}")
    public void bulkUpload(MultipartFile file, @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("type") String type ){
        applicationService.bulkUpload(file, userPrincipal,type);
    }
}
