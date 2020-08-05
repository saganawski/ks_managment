package com.ks.management.recruitment.interview.service;

import com.ks.management.employee.Employee;
import com.ks.management.employee.dao.JpaEmployeeRepo;
import com.ks.management.office.Office;
import com.ks.management.office.dao.JpaOfficeRepo;
import com.ks.management.recruitment.application.ApplicationContactType;
import com.ks.management.recruitment.application.ApplicationResult;
import com.ks.management.recruitment.application.ApplicationSource;
import com.ks.management.recruitment.application.dao.ApplicationContactTypeJpaDao;
import com.ks.management.recruitment.application.dao.ApplicationResultJpaDao;
import com.ks.management.recruitment.application.dao.ApplicationSourceJpaDao;
import com.ks.management.recruitment.application.ui.ApplicationFormOptionsDto;
import com.ks.management.recruitment.interview.Interview;
import com.ks.management.recruitment.interview.InterviewConfirmationType;
import com.ks.management.recruitment.interview.InterviewNote;
import com.ks.management.recruitment.interview.InterviewResult;
import com.ks.management.recruitment.interview.dao.JpaInterview;
import com.ks.management.recruitment.interview.dao.JpaInterviewConfirmationType;
import com.ks.management.recruitment.interview.dao.JpaInterviewNote;
import com.ks.management.recruitment.interview.dao.JpaInterviewResult;
import com.ks.management.recruitment.interview.ui.InterviewApplicationDto;
import com.ks.management.recruitment.interview.ui.InterviewDto;
import com.ks.management.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    @Autowired
    private JpaOfficeRepo jpaOfficeRepo;
    @Autowired
    private JpaInterviewNote jpaInterviewNote;

    @Override
    public List<Interview> getAllInterviews() {
        return jpaInterview.findAll();
    }

    @Override
    public Interview createInterview(Interview interview, UserPrincipal userPrincipal) {
        final Integer activeUserId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        final Employee scheduler = jpaEmployeeRepo.findById(2).orElse(null);
        if(scheduler == null){
            throw  new RuntimeException("Could not find employee to set a scheduler!");
        }
        interview.setScheduler(scheduler);
        interview.setCreatedBy(activeUserId);
        interview.setUpdatedBy(activeUserId);

        return jpaInterview.save(interview);
    }

    @Override
    public InterviewApplicationDto findInterviewApplicationDto(int interviewId) {
        final Interview interview = jpaInterview.findById(interviewId).orElse(null);

        if(interview == null){
            throw new RuntimeException("Error: Could not find interview with Id: " +interviewId);
        }
        //Offices
        final Set<Office> offices = new HashSet<>(jpaOfficeRepo.findAll());
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
                .officeOptions(offices)
                .build();


        return dto;
    }

    @Override
    public Interview updateInterview(InterviewDto interviewDto, UserPrincipal userPrincipal) {
        final Integer activeUserId = Optional.ofNullable(userPrincipal.getUserId()).orElse(-1);
        final Interview interview = jpaInterview.findById(interviewDto.getId()).orElse(null);
        if(interview == null){
            throw new RuntimeException("Could not find interview object with ID :"+ interviewDto.getId());
        }
        final List<Employee> interviewers = jpaEmployeeRepo.findAllById(interviewDto.getInterviewersId());
        interview.setUpdatedBy(activeUserId);
        interview.setScheduledTime(interviewDto.getScheduledTime());
        interview.setInterviewConfirmationType(interviewDto.getInterviewConfirmationType());
        interview.setInterviewResult(interviewDto.getInterviewResult());
        interview.setScheduler(interviewDto.getScheduler());
        interview.setInterviewers(interviewers);
        if(interviewDto.getInterviewNotes() != null){
            final InterviewNote note = interviewDto.getInterviewNotes().get(0);
            note.setCreatedBy(activeUserId);
            note.setUpdatedBy(activeUserId);
            note.setInterview(interview);
            interview.addInterviewNote(note);
        }
        return jpaInterview.save(interview);
    }

    @Override
    public void deleteNote(int noteId) {
        jpaInterviewNote.deleteById(noteId);
    }

    @Override
    public void deleteInterview(int interviewId) {
        jpaInterview.deleteById(interviewId);
    }

    @Override
    public Boolean checkIfInterviewExistsByApplicationId(Integer applicationId) {
        final List<Interview> interviews = jpaInterview.findAllByApplicationId(applicationId);
        Boolean exists = false;
        if(!interviews.isEmpty()){
            exists = true;
        }
        return exists;
    }
}
