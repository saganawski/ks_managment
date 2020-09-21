package com.ks.management.project.service;

import com.ks.management.project.Project;
import com.ks.management.project.ProjectWeek;
import com.ks.management.project.ui.ProjectDTO;
import com.ks.management.security.UserPrincipal;

import java.util.Set;

public interface ProjectService {
    Set<Project> getProjects();

    Project createProject(Project project, UserPrincipal userPrincipal);

    ProjectDTO getProjectDtoById(Integer projectId);

    ProjectDTO createNewWeekForProject(Integer projectId, UserPrincipal userPrincipal);

    ProjectWeek updateProjectWeek(Integer projectId, UserPrincipal userPrincipal, ProjectWeek projectWeek);

    Project markComplete(Integer projectId, UserPrincipal userPrincipal);

    void deleteProjectWeek(Integer projectId, Integer projectWeekId, UserPrincipal userPrincipal);
}
