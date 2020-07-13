package com.ks.management.recruitment.application.controller;

import com.ks.management.recruitment.application.ApplicationSource;
import com.ks.management.recruitment.application.dao.ApplicationSourceJpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applicationSources")
public class ApplicationSourceController {
    @Autowired
    private ApplicationSourceJpaDao applicationSourceJpaDao;

    @GetMapping()
    public List<ApplicationSource> getApplicationSources(){
        return applicationSourceJpaDao.findAll();
    }
}
