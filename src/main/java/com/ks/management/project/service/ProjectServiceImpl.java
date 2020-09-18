package com.ks.management.project.service;

import com.ks.management.project.Project;
import com.ks.management.project.dao.JpaProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private JpaProjectRepo jpaProjectRepo;

    @Override
    public Set<Project> getProjects() {
        return jpaProjectRepo.findAll().stream().collect(Collectors.toSet());
    }
}
