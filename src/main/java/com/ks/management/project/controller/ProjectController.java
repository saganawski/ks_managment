package com.ks.management.project.controller;

import com.ks.management.project.Project;
import com.ks.management.project.ProjectWeek;
import com.ks.management.project.service.ProjectService;
import com.ks.management.project.ui.ProjectDTO;
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

    @GetMapping("/{projectId}/dto")
    public ProjectDTO getProjectDtoById(@PathVariable("projectId") Integer projectId){
        return projectService.getProjectDtoById(projectId);
    }

    @PostMapping("/{projectId}/new-week")
    public ProjectDTO createNewWeekForProject(@PathVariable("projectId") Integer projectId, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return projectService.createNewWeekForProject(projectId,userPrincipal);
    }

    @PutMapping("/{projectId}/week")
    public ProjectWeek updateProjectWeek(@PathVariable("projectId") Integer projectId, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody ProjectWeek projectWeek){
        return projectService.updateProjectWeek(projectId,userPrincipal,projectWeek);
    }

}
