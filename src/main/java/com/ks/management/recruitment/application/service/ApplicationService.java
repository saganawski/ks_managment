package com.ks.management.recruitment.application.service;

import com.ks.management.recruitment.application.Application;
import com.ks.management.recruitment.application.ApplicationDto;

import java.util.List;

public interface ApplicationService {

    Application createApplication(Application application);

    List<ApplicationDto> findAll();
}
