package com.ks.management.report.service;

import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.recruitment.application.dao.ApplicationJpa;
import com.ks.management.recruitment.interview.dao.JpaInterview;
import com.ks.management.report.Conversion;
import com.ks.management.report.dao.JpaConversionDao;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
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
    @Autowired
    private JpaOfficeRepo jpaOfficeRepo;

    @Override
    public Conversion createConversionForOfficeByTimePeriod(Integer officeId, String startDate, String endDate, UserPrincipal userPrincipal) {
        final Integer userId = userPrincipal.getUserId();
        final LocalDate localStartDate = LocalDate.parse(startDate);
        final LocalDate localEndDate = LocalDate.parse(endDate);

        final Office office = jpaOfficeRepo.findById(officeId).orElse(null);

        final Integer applicationCount = applicationJpa.getCountOfApplicationsForOfficeBetweenDates(officeId,startDate,endDate);
        final Integer interviewsCount = jpaInterview.getInterviewsCountForOfficeBetweenDates(officeId,startDate,endDate);
        final Integer interviewsShowCount = jpaInterview.getInterviewsShowCountForOfficeBetweenDates(officeId,startDate,endDate);

        Double interviewShowRate = Double.valueOf(0);
        if(interviewsCount != 0 && interviewsShowCount !=0){
            interviewShowRate = Double.valueOf(interviewsShowCount) / Double.valueOf(interviewsCount);
        }
        final Integer totalHires = jpaInterview.getInterviewsHireCountForOfficeBetweenDates(officeId,startDate,endDate);

        Double hireRate = Double.valueOf(0);
        if(totalHires != 0 && interviewsShowCount !=0){
            hireRate = Double.valueOf(totalHires) /  Double.valueOf(interviewsShowCount);
        }

        final Conversion conversion = Conversion.builder()
                .startDate(localStartDate)
                .endDate(localEndDate)
                .office(office)
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
