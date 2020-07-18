package com.ks.management.recruitment.interview.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.recruitment.application.ApplicationContactType;
import com.ks.management.recruitment.application.ApplicationResult;
import com.ks.management.recruitment.application.ApplicationSource;
import com.ks.management.recruitment.application.dao.ApplicationContactTypeJpaDao;
import com.ks.management.recruitment.application.dao.ApplicationResultJpaDao;
import com.ks.management.recruitment.application.dao.ApplicationSourceJpaDao;
import com.ks.management.recruitment.application.ui.ApplicationFormOptionsDto;
import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.InterviewConfirmationType;
import com.ks.management.recruitment.interview.InterviewResult;
import com.ks.management.recruitment.interview.dao.JpaInterview;
import com.ks.management.recruitment.interview.dao.JpaInterviewConfirmationType;
import com.ks.management.recruitment.interview.dao.JpaInterviewResult;
import com.ks.management.recruitment.interview.ui.InterviewApplicationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {
    @Autowired
    private JpaInterview jpaInterview;

    @Autowired
    private JpaEmployeeRepo jpaEmployeeRepo;
    @Autowired
    private JpaInterviewConfirmationType jpaInterviewConfirmationType;
    @Autowired
    private JpaInterviewResult jpaInterviewResult;
    @Autowired
    private ApplicationContactTypeJpaDao applicationContactTypeJpaDao;
    @Autowired
    private ApplicationSourceJpaDao applicationSourceJpaDao;
    @Autowired
    private ApplicationResultJpaDao applicationResultJpaDao;

    @Override
    public List<Interview> getAllInterviews() {
        return jpaInterview.findAll();
    }

    @Override
    public Interview createInterview(Interview interview) {
        //TODO: find active user as employee and createdBy / updatedBy
        final Employee scheduler = jpaEmployeeRepo.findById(2).orElse(null);
        if(scheduler == null){
            throw  new RuntimeException("Could not find employee to set a scheduler!");
        }
        interview.setScheduler(scheduler);
        interview.setCreatedBy(-1);
        interview.setUpdatedBy(-1);

        return jpaInterview.save(interview);
    }

    @Override
    public InterviewApplicationDto findInterviewApplicationDto(int interviewId) {
        final Interview interview = jpaInterview.findById(interviewId).orElse(null);

        if(interview == null){
            throw new RuntimeException("Error: Could not find interview with Id: " +interviewId);
        }
        //InterView Forms
        final Set<Employee> employees = new HashSet<>(jpaEmployeeRepo.findAllNonCanvassers());
        final Set<InterviewConfirmationType> types = new HashSet<>(jpaInterviewConfirmationType.findAll());
        final Set<InterviewResult> results = new HashSet<>(jpaInterviewResult.findAll());
        //Application Forms

        final Set<ApplicationContactType> contactTypes = new HashSet<>(applicationContactTypeJpaDao.findAll());
        final Set<ApplicationSource> applicationSources = new HashSet<>(applicationSourceJpaDao.findAll());
        final Set<ApplicationResult>applicationResults = new HashSet<>(applicationResultJpaDao.findAll());
        final ApplicationFormOptionsDto applicationFormOptionsDto = ApplicationFormOptionsDto.builder()
                .applicationContactTypes(contactTypes)
                .applicationSources(applicationSources)
                .applicationResults(applicationResults)
                .build();


        final InterviewApplicationDto dto = InterviewApplicationDto.builder()
                .interviewId(interview.getId())
                .application(interview.getApplication())
                .scheduledTime(interview.getScheduledTime())
                .scheduler(interview.getScheduler())
                .interviewConfirmationType(interview.getInterviewConfirmationType())
                .interviewResult(interview.getInterviewResult())
                .interviewers(interview.getInterviewers())
                .interviewNotes(interview.getInterviewNotes())
                .updatedBy(interview.getUpdatedBy())
                .updatedDate(interview.getUpdatedDate())
                .createdBy(interview.getCreatedBy())
                .createdDate(interview.getCreatedDate())
                // Set options for forms
                .schedulersOptions(employees)
                .interviewersOptions(employees)
                .confirmationTypesOptions(types)
                .interviewResultsOptions(results)
                .applicationFormOptionsDto(applicationFormOptionsDto)
                .build();


        return dto;
    }
}
