package com.ks.management.recruitment.application.controller;

import com.ks.management.recruitment.application.ApplicationResult;
import com.ks.management.recruitment.application.dao.ApplicationResultJpaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applicationResults")
public class ApplicationResultController {
    @Autowired
    private ApplicationResultJpaDao applicationResultJpaDao;

    @GetMapping()
    public List<ApplicationResult> getResults(){
        return applicationResultJpaDao.findAll();
    }
}
