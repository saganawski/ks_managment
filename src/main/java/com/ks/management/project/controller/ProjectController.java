package com.ks.management.project.controller;

import com.ks.management.project.Project;
import com.ks.management.project.service.ProjectService;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping()
    public Set<Project> getProjects(){
        return projectService.getProjects();
    }

    @PostMapping()
    public Project createProject(@RequestBody Project project, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return projectService.createProject(project, userPrincipal);
    }

}
