package com.ks.management.recruitment.application.controller;

import com.ks.management.recruitment.application.ApplicationContactType;
import com.ks.management.recruitment.application.dao.ApplicationContactTypeJpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applicationContactTypes")
public class ApplicationContactTypeController {
    @Autowired
    private ApplicationContactTypeJpaDao applicationContactTypeJpaDao;

    @GetMapping()
    public List<ApplicationContactType> getApplicationContactTypes(){
        return  applicationContactTypeJpaDao.findAll();
    }
}
