package com.ks.management.recruitment.application.service;

import com.ks.management.recruitment.application.*;
import com.ks.management.recruitment.application.dao.ApplicationJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private ApplicationJpa applicationJpa;

    @Override
    public Application createApplication(Application application) {
        return applicationJpa.save(application);
    }

    @Override
    public List<ApplicationDto> findAll() {
        List<Application> applications = applicationJpa.findAll();
        List<ApplicationDto> applicationDtos = applications.stream()
                .map(a ->{
                    final Integer id = Optional.ofNullable(a.getId()).orElse(null);
                    final String firstName = Optional.ofNullable(a.getFirstName()).orElse("");
                    final String lastName = Optional.ofNullable(a.getLastName()).orElse("");
                    final String phoneNumber =Optional.ofNullable(a.getPhoneNumber()).orElse("");
                    final String email = Optional.ofNullable(a.getEmail()).orElse("");
                    final Date dateReceived = Optional.ofNullable(a.getDateReceived()).orElse(null);
                    final Date callBackDate = Optional.ofNullable(a.getCallBackDate()).orElse(null);
                    final Integer updatedBy = Optional.ofNullable(a.getUpdatedBy()).orElse(-1);
                    final Date updatedDate = Optional.ofNullable(a.getUpdatedDate()).orElse(null);
                    final Integer createdBy = Optional.ofNullable(a.getCreatedBy()).orElse(null);
                    final Date createdDate = Optional.ofNullable(a.getCreatedDate()).orElse(null);
                    final ApplicationContactType applicationContactType = Optional.ofNullable(a.getApplicationContactType()).orElse(null);
                    final ApplicationSource applicationSource = Optional.ofNullable(a.getApplicationSource()).orElse(null);
                    final ApplicationResult applicationResult = Optional.ofNullable(a.getApplicationResult()).orElse(null);

                    return ApplicationDto.builder()
                            .id(id)
                            .firstName(firstName)
                            .lastName(lastName)
                            .phoneNumber(phoneNumber)
                            .email(email)
                            .dateReceived(dateReceived)
                            .callBackDate(callBackDate)
                            .updatedBy(updatedBy)
                            .updatedDate(updatedDate)
                            .createdBy(createdBy)
                            .createdDate(createdDate)
                            .applicationContactType(applicationContactType)
                            .applicationSource(applicationSource)
                            .applicationResult(applicationResult)
                            .build();
                }).collect(Collectors.toList());
        return applicationDtos;
    }
}
