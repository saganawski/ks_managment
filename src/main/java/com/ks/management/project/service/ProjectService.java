package com.ks.management.project.service;

import com.ks.management.project.Project;
import com.ks.management.security.UserPrincipal;

import java.util.Set;

public interface ProjectService {
    Set<Project> getProjects();

    Project createProject(Project project, UserPrincipal userPrincipal);
}
