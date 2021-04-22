package com.ks.management.report.service;

import com.ks.management.recruitment.application.dao.ApplicationJpa;
import com.ks.management.recruitment.interview.dao.JpaInterview;
import com.ks.management.report.Conversion;
import com.ks.management.report.dao.JpaConversionDao;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@Transactional
public class ConversionServiceImpl implements ConversionService {
    @Autowired
    private JpaConversionDao jpaConversionDao;
    @Autowired
    private ApplicationJpa applicationJpa;
    @Autowired
    private JpaInterview jpaInterview;

    @Override
    public Conversion createConversionForOfficeByTimePeriod(Integer officeId, String startDate, String endDate, UserPrincipal userPrincipal) {
        final Integer userId = userPrincipal.getUserId();
        final LocalDate localStartDate = LocalDate.parse(startDate);
        final LocalDate localEndDate = LocalDate.parse(endDate);

        final Integer applicationCount = applicationJpa.getCountOfApplicationsForOfficeBetweenDates(officeId,startDate,endDate);
        final Integer interviewsCount = jpaInterview.getInterviewsCountForOfficeBetweenDates(officeId,startDate,endDate);
        final Integer interviewsShowCount = jpaInterview.getInterviewsShowCountForOfficeBetweenDates(officeId,startDate,endDate);
        final Double interviewShowRate = Double.valueOf(interviewsShowCount) / Double.valueOf(interviewsCount);
        final Integer totalHires = jpaInterview.getInterviewsHireCountForOfficeBetweenDates(officeId,startDate,endDate);
        final Double hireRate = Double.valueOf(totalHires) /  Double.valueOf(interviewsShowCount);

        final Conversion conversion = Conversion.builder()
                .startDate(localStartDate)
                .endDate(localEndDate)
                .totalApplications(applicationCount)
                .totalInterviewsScheduled(interviewsCount)
                .totalInterviewsShow(interviewsShowCount)
                .interviewShowRate(interviewShowRate)
                .totalHires(totalHires)
                .interviewHireRate(hireRate)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        return jpaConversionDao.save(conversion);
    }

    @Override
    public List<Conversion> getAllConversions() {
        return jpaConversionDao.findAll();
    }
}
